package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.CheckTimeLineAdapter
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogScheduleDetailsBinding
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.extension.acquire
import com.keycome.twinkleschedule.model.ScheduleDetailsViewModel
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.sketch.TimeLine
import kotlinx.coroutines.launch

class ScheduleDetailsDialog : BaseDialogFragment() {

    private var _binding: DialogScheduleDetailsBinding? = null
    val binding get() = _binding.acquire()

    private val viewModel by viewModels<ScheduleDetailsViewModel>()

    private val navController by lazy { findNavController() }

    private val event: (TimeLine) -> Unit = {}

    private val timeLineAdapter = CheckTimeLineAdapter(event)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        requestBottomFullDialog()
        _binding = DialogScheduleDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.firstPresent) {
            arguments?.getLong(scheduleIdKey)?.let {
                viewModel.querySchedule(it)
            }
            viewModel.firstPresent = false
        }
        binding.dialogScheduleDetailsRecyclerView.adapter = timeLineAdapter
        binding.dialogScheduleDetailsRecyclerView.layoutManager =
            LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        viewModel.liveSchedule.observe(viewLifecycleOwner) {
            binding.dialogScheduleDetailsTitle.text = it.name
            binding.dialogScheduleDetailsDate.text = it.schoolBeginDate.toDotDateString()
            binding.dialogScheduleDetailsCount.text = it.dailyCourses.toString()
            binding.dialogScheduleDetailsDay.text = it.weeklyEndDay.name
            binding.dialogScheduleDetailsDuration.text = it.courseDuration.toString()
            timeLineAdapter.submitList(it.timeLine.toList())
        }
        binding.dialogScheduleDetailsCancel.setOnClickListener {
            navController.navigateUp()
        }
        binding.dialogScheduleDetailsDelete.setOnClickListener {
            lifecycleScope.launch {
                val id = viewModel.queryScheduleId()
                if (id != 0L) {
                    viewModel.deleteSchedule(id)
                    Pipette.pipetteForString.emit(Drop("schedule_operation", "delete"))
                    navController.navigateUp()
                }
            }
        }
        binding.dialogScheduleDetailsModify.setOnClickListener {
            val id = viewModel.queryScheduleId()
            if (id != 0L) {
                emit("edit_schedule_fragment") {
                    putLong("schedule_id", id)
                }
                navController.navigate(
                    R.id.action_scheduleDetailsDialog_to_editScheduleFragment2
                )
            }
        }
        binding.dialogScheduleDetailsUse.setOnClickListener {
            val id = viewModel.queryScheduleId()
            if (id != 0L) {
                GlobalPreference.displayScheduleId.value = id
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val scheduleIdKey = "schedule_details_dialog_schedule_id"
    }
}