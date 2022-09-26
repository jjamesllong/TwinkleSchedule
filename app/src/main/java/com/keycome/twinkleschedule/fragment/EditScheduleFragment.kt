package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.RoutinesNameAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.extension.days.toLocalWord
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_SECTION_SIZE
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_ID
import com.keycome.twinkleschedule.viewmodel.EditScheduleViewModel

class EditScheduleFragment : BaseFragment() {

    private var _binding: FragmentEditScheduleBinding? = null
    val binding get() = _binding!!

    private val navController: NavController by lazy { findNavController() }

    val viewModel by viewModels<EditScheduleViewModel>()

    private val scheduleId by lazy { arguments?.getLong(KEY_SCHEDULE_ID) ?: 0L }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditScheduleBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RoutinesNameAdapter { v, r ->
            when (v.id) {
                R.id.cell_routines_name_name -> {
                    navController.navigate(
                        R.id.action_editScheduleFragment_to_editRoutineFragment,
                        Bundle().apply {
                            putInt(KEY_ROUTINE_SECTION_SIZE, viewModel.liveEndSection.value ?: 0)
                            putParcelable(KEY_ROUTINE, r)
                        }
                    )
                }
                R.id.cell_routines_name_delete -> {
                }
                else -> {}
            }
        }

        binding.editScheduleTitle.text = if (scheduleId == 0L) getString(
            R.string.fragment_edit_schedule_new
        ) else getString(
            R.string.fragment_edit_schedule_modify
        )
        binding.editScheduleNavigation.setOnClickListener {
            navController.navigateUp()
        }
        binding.editScheduleSubmission.setOnClickListener {

        }
        binding.editScheduleNameItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_scheduleNameDialog)
        }
        binding.editScheduleStartDateItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_scheduleStartDateDialog)
        }
        binding.editScheduleEndSectionItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_scheduleEndSectionDialog)
        }
        binding.editScheduleEndDayItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_scheduleEndDayDialog)
        }
        binding.editScheduleEndWeekItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_scheduleEndWeekDialog)
        }
        binding.editScheduleNewRoutine.setOnClickListener {
            navController.navigate(
                R.id.action_editScheduleFragment_to_editRoutineFragment,
                Bundle().apply {
                    putInt(KEY_ROUTINE_SECTION_SIZE, viewModel.liveEndSection.value ?: 0)
                    putParcelable(KEY_ROUTINE, viewModel.newRoutine())
                }
            )
        }
        binding.editScheduleRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.editScheduleRecyclerView.adapter = adapter
        binding.editScheduleToDisplayItem.setOnClickListener {
            binding.editScheduleToDisplaySwitch.toggle()
        }

        viewModel.liveScheduleName.observe(viewLifecycleOwner) {
            binding.editScheduleNameText.text = it
        }
        viewModel.liveStartDate.observe(viewLifecycleOwner) {
            binding.editScheduleStartDateText.text = it.toDotDateString()
        }
        viewModel.liveEndSection.observe(viewLifecycleOwner) {
            binding.editScheduleEndSectionText.text = it.toString()
        }
        viewModel.liveEndDay.observe(viewLifecycleOwner) {
            with(requireContext()) {
                binding.editScheduleEndDayText.text = Day.fromNumber(it).toLocalWord()
            }
        }
        viewModel.liveEndWeek.observe(viewLifecycleOwner) {
            binding.editScheduleEndWeekText.text = it.toString()
        }
        viewModel.liveRoutines.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.onFirstPresent {
            if (scheduleId != 0L) {
                viewModel.loadScheduleFromDatabase(scheduleId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val KEY_EDIT_SCHEDULE_ID = "EDIT_SCHEDULE_ID"
    }
}