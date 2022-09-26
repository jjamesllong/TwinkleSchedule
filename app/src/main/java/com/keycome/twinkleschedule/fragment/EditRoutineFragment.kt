package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.EditRoutineAdapter
import com.keycome.twinkleschedule.adapter.EditRoutineHeaderAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditRoutineBinding
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.timetable.Routine
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_SECTION_DURATION
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_SECTION_INDEX
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_SECTION_SIZE
import com.keycome.twinkleschedule.util.dialogs.DatePickerDialog
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import com.keycome.twinkleschedule.util.dialogs.TimePickerDialog
import com.keycome.twinkleschedule.viewmodel.EditRoutineViewModel
import kotlinx.coroutines.launch

class EditRoutineFragment : BaseFragment() {

    private var _binding: FragmentEditRoutineBinding? = null
    val binding get() = _binding.acquire()

    private val navController by lazy { findNavController() }

    val viewModel by viewModels<EditRoutineViewModel>()

    private val headerClickAction: (View) -> Unit = {
        when (it.id) {
            R.id.view_edit_routine_name_item -> {
                navController.navigate(
                    R.id.action_editRoutineFragment_to_routineNameDialog,
                    Bundle().apply {
                        viewModel.liveRoutineName.value?.also { name ->
                            putString(EditTextDialog.KEY_TEXT, name)
                        }
                    }
                )
            }
            R.id.view_edit_routine_start_date_item -> {
                navController.navigate(
                    R.id.action_editRoutineFragment_to_routineStartDateDialog,
                    Bundle().apply {
                        viewModel.liveStartDate.value?.also { date ->
                            putString(DatePickerDialog.KEY_DATE_SELECTED, date.toString())
                        }
                    }
                )
            }
            R.id.view_edit_routine_duration_item -> {
                navController.navigate(
                    R.id.action_editRoutineFragment_to_routineDurationDialog,
                    Bundle().apply {
                        viewModel.liveSectionDuration.value?.also { duration ->
                            putInt(KEY_ROUTINE_SECTION_DURATION, duration)
                        }
                    }
                )
            }
        }
    }

    private val bodyClickAction: (View, Int) -> Unit = { view, index ->
        when (view.id) {
            R.id.cell_section_list_root -> {
                navController.navigate(
                    R.id.action_editRoutineFragment_to_routineSectionTimeDialog,
                    Bundle().apply {
                        putInt(KEY_ROUTINE_SECTION_INDEX, index)
                        viewModel.liveSectionList.value?.get(index)?.also { section ->
                            putString(TimePickerDialog.KEY_TIME_SELECTED, section.from.toString())
                        }
                    }
                )
            }
            R.id.cell_section_list_delete -> {
                viewModel.deleteSectionByIndex(index)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRoutineBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onFirstPresent {
            arguments?.also { bundle ->
                viewModel.sectionSize = bundle.getInt(KEY_ROUTINE_SECTION_SIZE)
                bundle.getParcelable<Routine>(KEY_ROUTINE)?.also { routine ->
                    viewModel.routineId = routine.routineId
                    viewModel.masterId = routine.masterId
                    viewModel.refreshRoutineName(routine.routineName)
                    viewModel.refreshStartDate(Date.fromString(routine.startDate))
                    viewModel.refreshSectionListByString(routine.sectionList)
                }
            }
        }
        binding.fragmentEditRoutineToolbar.setOnMenuItemClickListener { menuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.edit_routine_toolbar_check -> {
                    lifecycleScope.launch {
                        val condition = viewModel.submitRoutine()
                        if (condition) {
                            navController.navigateUp()
                        } else {
                            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                else -> false
            }
        }
        binding.fragmentEditRoutineToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        val headerAdapter = EditRoutineHeaderAdapter(headerClickAction)
        val bodyAdapter = EditRoutineAdapter(bodyClickAction)
        val concatAdapter = ConcatAdapter(headerAdapter, bodyAdapter)
        binding.fragmentEditRoutineRecyclerView.adapter = concatAdapter
        binding.fragmentEditRoutineRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.fragmentEditRoutineNewSection.setOnClickListener {
            navController.navigate(
                R.id.action_editRoutineFragment_to_routineSectionTimeDialog,
                Bundle().apply { putInt(KEY_ROUTINE_SECTION_INDEX, -1) }
            )
        }

        viewModel.liveRoutineName.observe(viewLifecycleOwner) {
            headerAdapter.refreshName(it)
        }
        viewModel.liveStartDate.observe(viewLifecycleOwner) {
            headerAdapter.refreshDate(it)
        }
        viewModel.liveSectionDuration.observe(viewLifecycleOwner) {
            headerAdapter.refreshDuration(it)
        }
        viewModel.liveSectionList.observe(viewLifecycleOwner) {
            bodyAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}