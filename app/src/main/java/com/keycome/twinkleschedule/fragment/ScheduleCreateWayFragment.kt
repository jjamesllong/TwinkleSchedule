package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentScheduleCreateWayBinding

class ScheduleCreateWayFragment : BaseFragment() {

    private var _binding: FragmentScheduleCreateWayBinding? = null
    val binding get() = _binding!!

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleCreateWayBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentScheduleCreateWayToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        binding.fragmentScheduleCreateWayManual.setOnClickListener {
            navController.navigate(R.id.action_scheduleCreateWayFragment_to_editScheduleFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}