package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditCourseBinding
import com.keycome.twinkleschedule.dialog.CourseDayDialog
import com.keycome.twinkleschedule.dialog.CourseSectionDialog
import com.keycome.twinkleschedule.model.EditCourseViewModel
import kotlinx.coroutines.launch

class EditCourseFragment : BaseFragment() {

    private var _binding: FragmentEditCourseBinding? = null
    val binding get() = _binding.acquire()

    private val viewModel by viewModels<EditCourseViewModel>()

    private val navController by lazy { findNavController() }

    private val parentScheduleId by lazy { arguments?.getLong(scheduleIdKey) ?: 0L }
    private val isUpdate by lazy { arguments?.getBoolean(isUpdateKey) ?: false }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCourseBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentEditCourseToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        binding.editCourseFragmentTitleItem.setOnClickListener {
            navController.navigate(R.id.action_editCourseFragment_to_courseNameDialog)
        }
        binding.editCourseFragmentDayItem.setOnClickListener {
            navController.navigate(
                R.id.action_editCourseFragment_to_courseDayDialog,
                Bundle().apply {
                    putInt(CourseDayDialog.DayInItem, viewModel.liveDay.value?.toNumber() ?: 0)
                }
            )
        }
        binding.editCourseFragmentSectionItem.setOnClickListener {
            navController.navigate(
                R.id.action_editCourseFragment_to_courseSectionDialog,
                Bundle().apply {
                    putInt(
                        CourseSectionDialog.StartSectionInItem,
                        viewModel.liveSection.value?.first() ?: 0
                    )
                    putInt(
                        CourseSectionDialog.EndSectionInItem,
                        viewModel.liveSection.value?.last() ?: 0
                    )
                }
            )
        }
        binding.editCourseFragmentWeekItem.setOnClickListener {
            navController.navigate(R.id.action_editCourseFragment_to_courseWeekDialog)
        }
        binding.editCourseFragmentTeacherItem.setOnClickListener {
            navController.navigate(R.id.action_editCourseFragment_to_teacherNameDialog)
        }
        binding.editCourseFragmentClassroomItem.setOnClickListener {
            navController.navigate(R.id.action_editCourseFragment_to_classroomDialog)
        }
        binding.editCourseFragmentSubmitButton.setOnClickListener {
            it.isEnabled = false
            if (parentScheduleId != 0L) {
                lifecycleScope.launch {
                    val success = if (isUpdate) {
                        viewModel.updateCourse(parentScheduleId)
                    } else {
                        viewModel.insertCourse(parentScheduleId)
                    }
                    if (success) {
                        navController.navigateUp()
                    } else {
                        it.isEnabled = true
                        Toast.makeText(context, "错误", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        viewModel.liveTitle.observe(viewLifecycleOwner) {
            binding.editCourseTitle.text = it
        }
        viewModel.liveDay.observe(viewLifecycleOwner) {
            binding.editCourseDay.text = it.toString()
        }
        viewModel.liveSection.observe(viewLifecycleOwner) {
            val builder = StringBuilder()
            for (i in it.indices) {
                builder.append(it[i])
                builder.append(if (i != it.lastIndex) ", " else " 节")
            }
            binding.editCourseSection.text = builder.toString()
        }
        viewModel.liveWeek.observe(viewLifecycleOwner) {
            binding.editCourseWeek.text = it.toString()
        }
        viewModel.liveTeacher.observe(viewLifecycleOwner) {
            binding.editCourseTeacher.text = it
        }
        viewModel.liveClassroom.observe(viewLifecycleOwner) {
            binding.editCourseClassroom.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "EditCourseFragment"
        const val scheduleIdKey = "edit_course_fragment_schedule_id"
        const val isUpdateKey = "is_update_key"
    }
}