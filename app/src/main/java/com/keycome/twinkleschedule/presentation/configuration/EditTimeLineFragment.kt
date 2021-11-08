package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.DatePickerDialog
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.custom.TimePickerDialog
import com.keycome.twinkleschedule.custom.WheelDialog
import com.keycome.twinkleschedule.databinding.CellEditTimeLineBinding
import com.keycome.twinkleschedule.databinding.FragmentEditTimeLineBinding
import com.keycome.twinkleschedule.databinding.ViewEditTimeLineHeaderBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.LiveSchedule

class EditTimeLineFragment : BaseFragment<FragmentEditTimeLineBinding>() {

    private val viewModel: ConfigurationViewModel by activityViewModels()
    private val args by navArgs<EditTimeLineFragmentArgs>()

    private val editTextDialog: EditTextDialog by lazy {
        EditTextDialog(requireContext()) {
            onPositiveButtonPressed {
                textContent?.let {
                    viewModel.liveSchedule.updateTimeLineName(args.timeLineId, it)
                }
            }
        }
    }

    private val durationWheelDialog: WheelDialog by lazy {
        val list = mutableListOf<String>()
        (30..60).step(5).forEach { list.add(it.toString()) }
        WheelDialog(requireContext(), list) {
            onPositiveButtonPressed {
                viewModel.liveSchedule.updateValue(
                    LiveSchedule.course_duration,
                    currentValue.toInt()
                )
            }
        }
    }

    private val timePickerDialog: TimePickerDialog by lazy {
        TimePickerDialog(requireContext())
    }

    private val datePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(requireContext()) {
            onPositiveButtonPressed {
                viewModel.liveSchedule.updateTimeLineDate(args.timeLineId, currentDate)
            }
        }
    }

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditTimeLineBinding {
        return FragmentEditTimeLineBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): ViewToolbarLayoutBinding {
        title[0] = R.string.editTimeLineFragmentLabel
        return binding.fragmentAddTimeLineToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addTimeLineHeaderAdapter = EditTimeLineHeaderAdapter(
            null, args.timeLineId
        ) { binding, id -> headerAdapterOnclickHandler(binding, id) }
        val addTimeLineAdapter = EditTimeLineAdapter { binding, id, index ->
            adapterOnclickHandler(binding, id, index)
        }
        val concatAdapter = ConcatAdapter(addTimeLineHeaderAdapter, addTimeLineAdapter)
        binding.fragmentAddTimeLineRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
        binding.fragmentAddTimeLineBottomButton.setOnClickListener {
            timePickerDialog.apply {
                autoDismiss = false
                show()
                onPositiveButtonPressed {
                    val result =
                        viewModel.liveSchedule.addOrUpdateTimeLineListElement(
                            args.timeLineId,
                            currentTime,
                        )
                    if (!result) toast("列表中存在相同的时间")
                    dismiss()
                }
                onNegativeButtonPressed { dismiss() }
            }
        }
        viewModel.liveSchedule.observe(viewLifecycleOwner) {
            addTimeLineHeaderAdapter.apply {
                schedule = it
                notifyDataSetChanged()
            }
            it.timeLine.find { timeLine -> timeLine.id == args.timeLineId }?.let { timeLine ->
                addTimeLineAdapter.submitList(timeLine.lineList)
            }
        }
    }

    private fun headerAdapterOnclickHandler(binding: ViewEditTimeLineHeaderBinding, id: Int) {
        when (id) {
            binding.headerCurrentTimeLine.id -> editTextDialog.show()
            binding.headerCourseDuration.id -> durationWheelDialog.show()
            binding.headerStartDate.id -> datePickerDialog.show()
        }
    }

    private fun adapterOnclickHandler(binding: CellEditTimeLineBinding, id: Int, index: Int) {
        when (id) {
            binding.cellAddTimeLineDeleteButton.id -> {
                if (index == -1) return
                viewModel.liveSchedule.removeTimeLineListElement(args.timeLineId, index)
            }
            binding.root.id -> timePickerDialog.apply {
                autoDismiss = true
                show()
                onPositiveButtonPressed {
                    val result = viewModel.liveSchedule.addOrUpdateTimeLineListElement(
                        args.timeLineId,
                        currentTime,
                        index
                    )
                    if (!result) toast("列表中存在相同的时间")
                }
            }
        }
    }
}