package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.RoutinesNameAdapter
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogScheduleDetailsBinding
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.fragment.EditScheduleFragment
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Routine
import com.keycome.twinkleschedule.util.const.KEY_DISPLAY_SCHEDULE_ID
import com.keycome.twinkleschedule.viewmodel.ScheduleDetailsViewModel
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.launch

class ScheduleDetailsDialog : BaseDialogFragment() {

    private var _binding: DialogScheduleDetailsBinding? = null
    val binding get() = _binding.acquire()

    private val viewModel by viewModels<ScheduleDetailsViewModel>()

    private val navController by lazy { findNavController() }

    private val event: (View, Routine) -> Unit = { _, _ -> }

    private val adapter = RoutinesNameAdapter(event)

    override fun getDialogGravity(): Int {
        return GRAVITY_BOTTOM
    }

    override fun getDialogAnimation(): Int {
        return R.anim.slide_in_from_bottom
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogScheduleDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onFirstPresent {
            arguments?.getLong(KEY_SCHEDULE)?.let {
                viewModel.querySchedule(it)
            }
        }
        binding.dialogScheduleDetailsRecyclerView.adapter = adapter
        binding.dialogScheduleDetailsRecyclerView.layoutManager =
            LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        viewModel.liveSchedule.observe(viewLifecycleOwner) {
            binding.dialogScheduleDetailsTitle.text = it.scheduleName
            binding.dialogScheduleDetailsDate.text = it.startDate
            binding.dialogScheduleDetailsCount.text = it.endSection.toString()
            binding.dialogScheduleDetailsDay.text = Day.fromNumber(it.endDay).name
            binding.dialogScheduleDetailsWeeks.text = it.endWeek.toString()

        }
        binding.dialogScheduleDetailsCancel.setOnClickListener {
            navController.navigateUp()
        }
        binding.dialogScheduleDetailsDelete.setOnClickListener {
            lifecycleScope.launch {
                val id = viewModel.queryScheduleId()
                if (id != 0L) {
                    viewModel.deleteSchedule(id)
                    Pipette.forString.emit(Drop(KEY_SCHEDULE_OPERATION, DELETE))
                    navController.navigateUp()
                }
            }
        }
        binding.dialogScheduleDetailsModify.setOnClickListener {
            val id = viewModel.queryScheduleId()
            if (id != 0L) {
                navController.navigate(
                    R.id.action_scheduleDetailsDialog_to_editScheduleFragment,
                    Bundle().apply { putLong(EditScheduleFragment.KEY_EDIT_SCHEDULE_ID, id) }
                )
            }
        }
        binding.dialogScheduleDetailsUse.setOnClickListener {
            val id = viewModel.queryScheduleId()
            if (id != 0L) {
                MMKV.defaultMMKV().encode(KEY_DISPLAY_SCHEDULE_ID, id)
                navController.navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val KEY_SCHEDULE = "SCHEDULE"
        const val KEY_SCHEDULE_OPERATION = "SCHEDULE_OPERATION"
        const val DELETE = "DELETE"
    }
}