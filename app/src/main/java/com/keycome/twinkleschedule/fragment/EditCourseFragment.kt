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
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.dialog.CourseSectionDialog
import com.keycome.twinkleschedule.dialog.CourseWeekDialog
import com.keycome.twinkleschedule.extension.ints.toStringHex
import com.keycome.twinkleschedule.model.EditCourseViewModel
import com.keycome.twinkleschedule.record.interval.Day.Companion.toDay
import com.keycome.twinkleschedule.util.const.*
import kotlinx.coroutines.launch

class EditCourseFragment : BaseFragment() {

    private var _binding: FragmentEditCourseBinding? = null
    val binding get() = _binding.acquire()

    private val viewModel by viewModels<EditCourseViewModel>()

    private val navController by lazy { findNavController() }

    private val isUpdate by lazy { arguments?.getBoolean(KEY_IS_UPDATE) ?: false }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onFirstPresent {
            arguments?.also { bundle ->
                val scheduleId = bundle.getLong(KEY_SCHEDULE_ID)
                val endDay = bundle.getInt(KEY_SCHEDULE_END_DAY)
                val endSection = bundle.getInt(KEY_SCHEDULE_END_SECTION)
                val endWeek = bundle.getInt(KEY_SCHEDULE_END_WEEK)
                viewModel.setScheduleLimit(scheduleId, endDay, endSection, endWeek)
                if (isUpdate) {
                    val courseId = bundle.getLong(KEY_COURSE_ID)
                    val title = bundle.getString(KEY_COURSE_TITLE) ?: ""
                    val day = bundle.getString(KEY_COURSE_DAY)!!.toDay()
                    val section = bundle.getIntArray(KEY_COURSE_SECTION)!!.toList()
                    val week = bundle.getIntArray(KEY_COURSE_WEEK)!!.toList()
                    val teacher = bundle.getString(KEY_COURSE_TEACHER) ?: ""
                    val classroom = bundle.getString(KEY_COURSE_CLASSROOM) ?: ""
                    viewModel.setCourseInfo(
                        courseId,
                        title,
                        day,
                        section,
                        week,
                        teacher,
                        classroom
                    )
                }
            }
        }
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
                    putInt(KEY_SCHEDULE_END_DAY, viewModel.endDay)
                    putInt(KEY_COURSE_DAY, viewModel.liveDay.value?.toNumber() ?: 0)
                }
            )
        }
        binding.editCourseFragmentSectionItem.setOnClickListener {
            navController.navigate(
                R.id.action_editCourseFragment_to_courseSectionDialog,
                Bundle().apply {
                    putInt(KEY_SCHEDULE_END_SECTION, viewModel.endSection)
                    putInt(
                        CourseSectionDialog.KEY_START_SECTION,
                        viewModel.liveSection.value?.first() ?: 0
                    )
                    putInt(
                        CourseSectionDialog.KEY_END_SECTION,
                        viewModel.liveSection.value?.last() ?: 0
                    )
                }
            )
        }
        binding.editCourseFragmentWeekItem.setOnClickListener {
            navController.navigate(
                R.id.action_editCourseFragment_to_courseWeekDialog,
                Bundle().apply {
                    putInt(KEY_SCHEDULE_END_WEEK, viewModel.endWeek)
                    putInt(CourseWeekDialog.KEY_SELECTED_COURSE_WEEKS, viewModel.getSelectedWeek())
                }
            )
        }
        binding.editCourseFragmentTeacherItem.setOnClickListener {
            navController.navigate(R.id.action_editCourseFragment_to_teacherNameDialog)
        }
        binding.editCourseFragmentClassroomItem.setOnClickListener {
            navController.navigate(R.id.action_editCourseFragment_to_classroomDialog)
        }
        binding.editCourseFragmentColorItem.setOnClickListener {
            navController.navigate(
                R.id.action_editCourseFragment_to_courseColorFragment,
                Bundle().apply { putInt(KEY_COURSE_COLOR, viewModel.liveColor.value ?: 0) }
            )
        }
        binding.editCourseFragmentSubmitButton.setOnClickListener {
            it.isEnabled = false
            lifecycleScope.launch {
                val success = if (isUpdate) {
                    viewModel.updateCourse()
                } else {
                    viewModel.insertCourse()
                }
                if (success) {
                    Pipette.forEvent.distribute { KEY_COURSE_TABLE_CHANGE }
                    navController.navigateUp()
                } else {
                    it.isEnabled = true
                    Toast.makeText(
                        context,
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
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
        viewModel.liveColor.observe(viewLifecycleOwner) {
            binding.editCourseColor.text = it.toStringHex()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val KEY_IS_UPDATE = "is_update_key"
    }
}