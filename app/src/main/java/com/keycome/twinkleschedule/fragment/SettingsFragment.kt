package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addScheduleSettingItem.setOnClickListener {
            navController.navigate(R.id.action_settingsFragment_to_scheduleCreateWayFragment)
        }
        binding.manageSchedulesSettingItem.setOnClickListener {
            navController.navigate(
                R.id.action_settingsFragment_to_selectToManageScheduleFragment
            )
        }
        binding.recordCourseSettingItem.setOnClickListener {
            navController.navigate(R.id.action_settingsFragment_to_selectToAddCourseFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}