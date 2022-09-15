package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.CourseListAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentCourseListBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.util.const.*
import com.keycome.twinkleschedule.viewmodel.CourseListViewModel
import kotlinx.coroutines.launch

class CourseListFragment : BaseFragment() {

    private var _binding: FragmentCourseListBinding? = null
    private val binding get() = _binding.acquire()

    private val viewModel by viewModels<CourseListViewModel>()

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCourseListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.root.progress == 1f) {
                binding.root.transitionToStart()
                viewModel.refreshSelected(-1)
            } else {
                navController.navigateUp()
            }
        }
        val adapter = CourseListAdapter {
            when (binding.root.progress) {
                0f -> {
                    viewModel.refreshSelected(it)
                    binding.root.transitionToEnd()
                }
                1f -> {
                    viewModel.refreshSelected(-1)
                    binding.root.transitionToStart()
                }
            }
        }
        binding.fragmentCourseListToolbar.setNavigationOnClickListener {
            callback.handleOnBackPressed()
        }
        binding.courseListRecyclerView.itemAnimator?.changeDuration = 0
        binding.courseListRecyclerView.adapter = adapter
        binding.fragmentCourseListTextDelete.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.requestCourse()?.also { course ->
                    viewModel.deleteCourse(course)
                    binding.root.transitionToStart()
                    Pipette.forEvent.distribute { "" }
                }
            }
        }
        binding.fragmentCourseListTextModify.setOnClickListener {
            viewModel.requestCourse()?.also { course ->
                navController.navigate(
                    R.id.action_courseListFragment_to_editCourseFragment,
                    Bundle().apply {
                        putBoolean(EditCourseFragment.KEY_IS_UPDATE, true)
                        putLong(KEY_SCHEDULE_ID, course.masterId)
                        putLong(KEY_COURSE_ID, course.courseId)
                        putString(KEY_COURSE_TITLE, course.title)
                        putString(KEY_COURSE_DAY, Day.fromNumber(course.day).name)
                        putIntArray(KEY_COURSE_SECTION, course.section.toIntArray())
                        putIntArray(KEY_COURSE_WEEK, course.week.toIntArray())
                        putString(KEY_COURSE_TEACHER, course.teacher)
                        putString(KEY_COURSE_CLASSROOM, course.classroom)
                    }
                )
            }
        }
        viewModel.onFirstPresent {
            val id = arguments?.getLong(KEY_SCHEDULE_ID) ?: 0L
            if (id != 0L) {
                viewModel.scheduleId = id
                viewModel.queryCourseList(id)
            }
        }
        viewModel.liveCourseList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}