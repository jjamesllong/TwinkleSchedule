package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditCourseBinding

class EditCourseFragment : BaseFragment() {

    private var _binding: FragmentEditCourseBinding? = null
    val binding get() = _binding.acquire()

    private val navController by lazy { findNavController() }

    private val parentScheduleId by lazy { arguments?.getLong(scheduleIdKey) ?: 0L }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "EditCourseFragment"
        const val scheduleIdKey = "edit_course_fragment_schedule_id"
    }
}