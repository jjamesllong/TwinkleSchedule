package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseDetailsBinding
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.util.const.KEY_COURSE
import com.keycome.twinkleschedule.viewmodel.CourseDetailsViewModel

class CourseDetailsDialog : BaseDialogFragment() {

    val viewModel by viewModels<CourseDetailsViewModel>()

    private var _binding: DialogCourseDetailsBinding? = null
    val binding get() = _binding!!

    private val course by lazy { arguments?.getParcelable<Course>(KEY_COURSE) }

    override fun getDialogGravity(): Int {
        return GRAVITY_BOTTOM
    }

    override fun getDialogAnimations(): Int {
        return R.style.FullBottomDialogAnimation
    }

    override fun getDialogMode(): Int {
        return MODE_FILL
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogCourseDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.courseDetailsDialogCancel.setOnClickListener { dismiss() }
        course?.also {
            binding.courseDetailsDialogTitle.text = it.title
            binding.courseDetailsDialogDayText.text = Day.fromNumber(it.day).name
            binding.courseDetailsDialogSectionText.text = it.section.toString()
            binding.courseDetailsDialogClassroomText.text = it.classroom
            binding.courseDetailsDialogTeacherText.text = it.teacher
            binding.courseDetailsDialogWeekText.text = it.week.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}