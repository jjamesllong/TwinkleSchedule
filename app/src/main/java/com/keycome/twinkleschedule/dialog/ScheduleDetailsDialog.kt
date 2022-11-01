package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.RoutinesNameAdapter
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogScheduleDetailsBinding
import com.keycome.twinkleschedule.extension.days.toLocalWord
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Routine
import com.keycome.twinkleschedule.util.const.KEY_DISPLAY_SCHEDULE_ID
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_ID
import com.keycome.twinkleschedule.viewmodel.ScheduleDetailsViewModel
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.launch

class ScheduleDetailsDialog : BaseDialogFragment() {

    private var _binding: DialogScheduleDetailsBinding? = null
    val binding get() = _binding.acquire()

    private val viewModel by viewModels<ScheduleDetailsViewModel>()

    private val navController by lazy { findNavController() }

    private val event: (View, Routine) -> Unit = { _, _ -> }

    override fun getDialogGravity(): Int {
        return GRAVITY_BOTTOM
    }

    override fun getDialogMode(): Int {
        return MODE_FILL
    }

    override fun getDialogAnimations(): Int {
        return R.style.FullBottomDialogAnimation
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
            arguments?.getLong(KEY_SCHEDULE_ID)?.also {
                viewModel.querySchedule(it)
            }
        }

        val adapter = RoutinesNameAdapter(event)
        binding.dialogScheduleDetailsRoutines.adapter = adapter
        binding.dialogScheduleDetailsCancel.setOnClickListener {
            navController.navigateUp()
        }
        binding.dialogScheduleDetailsDelete.setOnClickListener {
            lifecycleScope.launch {
                val id = viewModel.queryScheduleId()
                if (id != 0L) {
                    viewModel.deleteSchedule(id)
                    navController.navigateUp()
                }
            }
        }
        binding.dialogScheduleDetailsModify.setOnClickListener {
            val id = viewModel.queryScheduleId()
            if (id != 0L) {
                navController.navigate(
                    R.id.action_scheduleDetailsDialog_to_editScheduleFragment,
                    Bundle().apply { putLong(KEY_SCHEDULE_ID, id) }
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

        viewModel.liveSchedule.observe(viewLifecycleOwner) {
            binding.dialogScheduleDetailsTitle.text = it.scheduleName
            binding.dialogScheduleDetailsDate.text = it.startDate
            binding.dialogScheduleDetailsEndSection.text = it.endSection.toString()
            binding.dialogScheduleDetailsEndDay.text = Day.fromNumber(it.endDay).toLocalWord(
                requireContext()
            )
            binding.dialogScheduleDetailsEndWeek.text = it.endWeek.toString()
        }
        viewModel.liveRoutines.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}