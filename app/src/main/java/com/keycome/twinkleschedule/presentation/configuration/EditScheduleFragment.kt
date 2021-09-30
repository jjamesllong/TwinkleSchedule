package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.DatePickerDialog
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.custom.EndDayDialog
import com.keycome.twinkleschedule.custom.WheelDialog
import com.keycome.twinkleschedule.databinding.CellTimeLineDescriptionBinding
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.model.LiveSchedule
import com.keycome.twinkleschedule.model.sketch.TimeLine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditScheduleFragment : BaseFragment<FragmentEditScheduleBinding>(), View.OnClickListener {

    private val viewModel: ConfigurationViewModel by activityViewModels()

    private val editTextDialog: EditTextDialog by lazy {
        EditTextDialog(requireContext()) {
            onPositiveButtonPressed {
                textContent?.let { viewModel.liveSchedule.updateValue(LiveSchedule.name_, it) }
            }
        }
    }

    private val datePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(requireContext()) {
            datePickerPosition = "2021-06-04"
            onPositiveButtonPressed {
                viewModel.liveSchedule.updateValue(
                    LiveSchedule.school_begin_date,
                    currentDate
                )
            }
        }
    }

    private val coursesWheelDialog: WheelDialog by lazy {
        val list = mutableListOf<String>()
        (1..16).forEach { list.add(it.toString()) }
        WheelDialog(requireContext(), list) {
            onPositiveButtonPressed {
                viewModel.liveSchedule.updateValue(
                    LiveSchedule.daily_courses,
                    currentValue.toInt()
                )
            }
        }
    }

    private val endDayDialog: EndDayDialog by lazy {
        EndDayDialog(requireContext()) {
            onItemSelected { viewModel.liveSchedule.updateValue(LiveSchedule.weekly_end_day, day) }
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

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditScheduleBinding {
        return FragmentEditScheduleBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding {
        title[0] = R.string.editScheduleFragmentLabel
        return binding.fragmentEditScheduleToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (child in binding.linearLayout.children)
            if (child.id != binding.timeLineRecyclerView.id)
                child.setOnClickListener(this)
        val timeLineAdapter = TimeLineAdapter(adapterOnClickHandler)
        val timeLineFooterAdapter = TimeLineFooterAdapter(footerAdapterOnClickHandler)
        val concatAdapter = ConcatAdapter(timeLineAdapter, timeLineFooterAdapter)
        binding.timeLineRecyclerView.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        viewModel.liveSchedule.observe(viewLifecycleOwner) {
            binding.scheduleNameText.text = it.name
            binding.schoolBeginDateText.text = it.schoolBeginDate.toDotDateString()
            binding.dailyCoursesText.text = it.dailyCourses.toString()
            binding.editEndDayText.text = it.weeklyEndDay.toString()
            binding.courseDurationText.text = it.courseDuration.toString()
            timeLineAdapter.submitList(it.timeLine.toMutableList())
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.editScheduleNameItem.id -> editTextDialog.show()
            binding.editSchoolBeginDateItem.id -> datePickerDialog.show()
            binding.editCourseNumberItem.id -> coursesWheelDialog.show()
            binding.editEndDayItem.id -> endDayDialog.show()
            binding.editCourseDurationItem.id -> durationWheelDialog.show()
            binding.editScheduleSubmitButton.id -> {
                viewModel.insertSchedule(false)
            }
        }
    }

    private val adapterOnClickHandler = fun(
        binding: CellTimeLineDescriptionBinding, viewId: Int, timeLine: TimeLine
    ) {
        when (viewId) {
            binding.root.id -> {
                val action =
                    EditScheduleFragmentDirections.actionEditScheduleFragmentToAddTimeLineFragment(
                        timeLine.id
                    )
                findNavController().navigate(action)
            }
            binding.cellTimeLineDescriptionDeleteButton.id -> {
                viewModel.liveSchedule.removeTimeLine(timeLine.id)
            }
        }
    }

    private val footerAdapterOnClickHandler = fun(_: View) {
        val action =
            EditScheduleFragmentDirections.actionEditScheduleFragmentToAddTimeLineFragment(
                viewModel.liveSchedule.createTimeLine()
            )
        lifecycleScope.launch {
            delay(500)
            findNavController().navigate(action)
        }
    }
}