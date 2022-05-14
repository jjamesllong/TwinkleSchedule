package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.custom.pagingtimetable.PagingAdapter
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.model.DisplayCoursesViewModel

class DisplayCoursesFragment : BaseFragment() {

    val viewModel by viewModels<DisplayCoursesViewModel>()

    private var _binding: FragmentDisplayCoursesBinding? = null
    val binding get() = _binding!!

    private val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDisplayCoursesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagingAdapter = PagingAdapter {
            viewModel.refreshDialogCourse(it.courseId)
            navController.navigate(R.id.action_displayCoursesFragment_to_courseDetailsDialog)
        }
        binding.tableViewPager.adapter = pagingAdapter
        binding.tableViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.refreshWeekSelected(position + 1)
                }
            }
        )
        viewModel.liveParentSchedule.observe(viewLifecycleOwner) {
            pagingAdapter.submitSchedule(it)
        }
        viewModel.liveSectionList.observe(viewLifecycleOwner) {
            pagingAdapter.submitSectionList(it)
        }
        viewModel.liveWeekSelected.observe(viewLifecycleOwner) {
            binding.fragmentDisplayCoursesToolbar.title = getString(
                R.string.fragment_display_courses_week,
                it
            )
            binding.fragmentDisplayCoursesToolbar.subtitle = if (
                it != viewModel.liveWeekNow.value
            ) {
                getString(R.string.fragment_display_courses_not_this_week)
            } else {
                null
            }
        }
        viewModel.livePagingCourseList.observe(viewLifecycleOwner) {
            pagingAdapter.submitCourseList(it)
            viewModel.liveWeekSelected.value?.let { week ->
                binding.tableViewPager.setCurrentItem(week - 1, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}