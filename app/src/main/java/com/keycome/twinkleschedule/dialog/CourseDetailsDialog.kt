package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseDetailsBinding
import com.keycome.twinkleschedule.model.CourseDetailsViewModel

class CourseDetailsDialog : BaseDialogFragment() {

    val viewModel by viewModels<CourseDetailsViewModel>()

    private var _binding: DialogCourseDetailsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        requestFullScreenBottomDialog()
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
        viewModel.sharedCourse?.observe(viewLifecycleOwner) {
            binding.courseDetailsDialogTitle.text = it.title
            binding.courseDetailsDialogDayText.text = it.day.name
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