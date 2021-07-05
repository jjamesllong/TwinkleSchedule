package com.keycome.twinkleschedule.presentation.configuration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.DatePickerDialog
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.custom.WheelDialog
import com.keycome.twinkleschedule.database.TimeLine
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.Date
import com.keycome.twinkleschedule.model.LiveSchedule

class EditScheduleFragment : BaseFragment<FragmentEditScheduleBinding>(), View.OnClickListener {
    private lateinit var safeContext: Context
    private val viewModel: ConfigurationViewModel by activityViewModels()

    private val editTextDialog: EditTextDialog by lazy {
        EditTextDialog(safeContext) {
            onPositiveButtonPressed {
                textContent?.let { viewModel.liveSchedule.updateValue(LiveSchedule.name_, it) }
            }
        }
    }

    private val datePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(safeContext) {
            datePickerPosition = "2021-06-04"
            onPositiveButtonPressed {
                viewModel.liveSchedule.updateValue(
                    LiveSchedule.school_begin_date,
                    Date(
                        currentDateStringList[0].toInt(),
                        currentDateStringList[1].toInt(),
                        currentDateStringList[2].toInt()
                    )
                )
            }
        }
    }

    private val coursesWheelDialog: WheelDialog by lazy {
        val list = mutableListOf<String>()
        (1..16).forEach { list.add(it.toString()) }
        WheelDialog(safeContext, list) {
            onPositiveButtonPressed {
                viewModel.liveSchedule.updateValue(
                    LiveSchedule.daily_courses,
                    currentValue.toInt()
                )
            }
        }
    }

    private val durationWheelDialog: WheelDialog by lazy {
        val list = mutableListOf<String>()
        (30..60).step(5).forEach { list.add(it.toString()) }
        WheelDialog(safeContext, list) {
            onPositiveButtonPressed {
                viewModel.liveSchedule.updateValue(
                    LiveSchedule.course_duration,
                    currentValue.toInt()
                )
            }
        }
    }

    override fun deployBinding(
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
        safeContext = requireContext()
        for (child in binding.linearLayout.children)
            if (child.id != binding.timeLineRecyclerView.id)
                child.setOnClickListener(this)
        val timeLineAdapter = TimeLineAdapter { adapterOnClickHandler(it) }
        binding.timeLineRecyclerView.apply {
            adapter = timeLineAdapter
            layoutManager = LinearLayoutManager(safeContext).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        viewModel.liveSchedule.observe(viewLifecycleOwner) {
            binding.scheduleNameText.text = it.name
            binding.schoolBeginDateText.text = it.schoolBeginDate.toDotDateString()
            binding.dailyCoursesText.text = it.dailyCourses.toString()
            binding.courseDurationText.text = it.courseDuration.toString()
            val timeLineSet = it.timeLine
            if (timeLineSet.isEmpty()) {
                if (binding.timeLineRecyclerView.visibility == View.VISIBLE)
                    binding.timeLineRecyclerView.visibility = View.GONE
            } else {
                val list = mutableListOf<TimeLine>()
                timeLineSet.forEach { t -> list.add(t) }
                timeLineAdapter.submitList(list)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.editScheduleNameItem.id -> editTextDialog.show()
            binding.editSchoolBeginDateItem.id -> datePickerDialog.show()
            binding.editCourseNumberItem.id -> coursesWheelDialog.show()
            binding.editCourseDurationItem.id -> durationWheelDialog.show()
            binding.editCourseTimeLineItem.id -> findNavController()
                .navigate(R.id.action_editScheduleFragment_to_addTimeLineFragment)
            binding.testButton.id -> viewModel.insertSchedule()
            binding.button2.id -> {
                binding.timeLineRecyclerView.visibility = View.GONE
            }
            else -> toast("none")
        }
    }

    private fun adapterOnClickHandler(timeLine: TimeLine) {
        val action =
            EditScheduleFragmentDirections.actionEditScheduleFragmentToAddTimeLineFragment(
                timeLine.id
            )
        findNavController().navigate(action)
    }
}