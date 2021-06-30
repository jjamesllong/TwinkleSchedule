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
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.database.TimeLine
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.Date
import com.keycome.twinkleschedule.model.Day

class EditScheduleFragment : BaseFragment<FragmentEditScheduleBinding>(), View.OnClickListener {

    private lateinit var safeContext: Context
    private val viewModel: ConfigurationViewModel by activityViewModels()
    private val scheduleMap = mutableMapOf<String, Any>()

    private val editTextDialog: EditTextDialog by lazy {
        EditTextDialog(safeContext) {
            onPositiveButtonPressed {
                textContent?.let { scheduleMap["name"] = it }
            }
        }
    }

    private val datePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(safeContext) {
            datePickerPosition = "2021-06-04"
            onPositiveButtonPressed {
                scheduleMap["schoolBeginDate"] = Date(
                    currentDateStringList[0].toInt(),
                    currentDateStringList[1].toInt(),
                    currentDateStringList[2].toInt()
                )
            }
        }
    }

    private val coursesWheelDialog: WheelDialog by lazy {
        val list = mutableListOf<String>()
        (1..16).forEach { list.add(it.toString()) }
        WheelDialog(safeContext, list) {
            onPositiveButtonPressed {
                scheduleMap["dailyCourses"] = currentValue.toInt()
            }
        }
    }

    private val durationWheelDialog: WheelDialog by lazy {
        val list = mutableListOf<String>()
        (30..60).step(5).forEach { list.add(it.toString()) }
        WheelDialog(safeContext, list) {
            onPositiveButtonPressed {
                scheduleMap["courseDuration"] = currentValue.toInt()
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
            child.setOnClickListener(this)
        val scheduleAdapter = ScheduleAdapter()
        binding.scheduleRecyclerView.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(safeContext).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                if (binding.scheduleRecyclerView.visibility == View.VISIBLE)
                    binding.scheduleRecyclerView.visibility = View.GONE
            }
            scheduleAdapter.submitList(it)
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
            binding.testButton.id -> {
                scheduleMap["weeklyEndDay"] = Day.Friday
                viewModel.insertSchedule(
                    ScheduleEntity(
                        scheduleId = 0,
                        name = scheduleMap["name"] as String,
                        schoolBeginDate = scheduleMap["schoolBeginDate"] as Date,
                        dailyCourses = scheduleMap["dailyCourses"] as Int,
                        weeklyEndDay = scheduleMap["weeklyEndDay"] as Day,
                        courseDuration = scheduleMap["courseDuration"] as Int,
                        timeLine = mapOf("test" to TestData.timeLine)
                    )
                )
            }
            binding.button2.id -> {
                binding.scheduleRecyclerView.visibility = View.GONE
            }
            else -> toast("none")
        }
    }
}