package com.keycome.twinkleschedule.presentation.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.keycome.twinkleschedule.databinding.FragmentCourseDetailDialogBinding

class CourseDetailDialogFragment : DialogFragment() {

    private var _binding: FragmentCourseDetailDialogBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel by activityViewModels<DisplayViewModel>()

    private val args by navArgs<CourseDetailDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailDialogBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.courseId
        val course = activityViewModel.getCourseById(id)
        course?.let {
            binding.courseDetailTitleText.text = it.title
            binding.courseDetailWeekText.text = it.week.toString()
            binding.courseDetailSectionText.text = it.section.toString()
            binding.courseDetailTeacherText.text = it.teacher
            binding.courseDetailClassroomText.text = it.classroom
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}