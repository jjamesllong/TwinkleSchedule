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
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.custom.TimePickerDialog
import com.keycome.twinkleschedule.custom.WheelDialog
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentAddTimeLineBinding
import com.keycome.twinkleschedule.databinding.ViewAddTimeLineHeaderBinding
import com.keycome.twinkleschedule.model.LiveSchedule

class AddTimeLineFragment : BaseFragment<FragmentAddTimeLineBinding>() {

    private val viewModel: ConfigurationViewModel by activityViewModels()
    private val args by navArgs<AddTimeLineFragmentArgs>()

    private val editTextDialog: EditTextDialog by lazy {
        EditTextDialog(requireContext()) {
            onPositiveButtonPressed {
                textContent?.let {
                    viewModel.liveSchedule.updateValue(
                        LiveSchedule.time_line_name,
                        it,
                        args.timeLineId
                    )
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

    val timePickerDialog: TimePickerDialog by lazy {
        TimePickerDialog(requireContext()) {
            timePickerPosition = arrayOf(17, 12)
        }
    }

    override fun deployBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddTimeLineBinding {
        return FragmentAddTimeLineBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding {
        title[0] = R.string.timeLineFragmentLabel
        return binding.fragmentAddTimeLineToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addTimeLineHeaderAdapter = AddTimeLineHeaderAdapter(
            null, args.timeLineId
        ) { binding, id -> headerAdapterOnclickHandler(binding, id) }
        val addTimeLineAdapter = AddTimeLineAdapter()
        val concatAdapter = ConcatAdapter(addTimeLineHeaderAdapter, addTimeLineAdapter)
        binding.fragmentAddTimeLineRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = concatAdapter
        }
        viewModel.liveSchedule.observe(viewLifecycleOwner) {
            addTimeLineHeaderAdapter.apply {
                schedule = it
                notifyDataSetChanged()
            }
            it.timeLine.find { timeLine -> timeLine.id == args.timeLineId }?.let { timeLine ->
                val list = mutableListOf<String>()
                timeLine.timeLineList.forEach { time ->
                    list.add(time.to24StyleString())
                }
                addTimeLineAdapter.submitList(list)
            }
        }
    }

    private fun headerAdapterOnclickHandler(binding: ViewAddTimeLineHeaderBinding, id: Int) {
        when (id) {
            binding.headerCurrentTimeLine.id -> editTextDialog.show()
            binding.headerCourseDuration.id -> durationWheelDialog.show()
            binding.headerCoursesNumber.id -> Unit
        }
    }
}