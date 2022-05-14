package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.TimePicker
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.EditDailyRoutineAdapter
import com.keycome.twinkleschedule.adapter.EditDailyRoutineHeaderAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditDailyRoutineBinding
import com.keycome.twinkleschedule.model.EditDailyRoutineViewModel
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.interval.Time
import com.keycome.twinkleschedule.record.timetable.DailyRoutine
import kotlinx.coroutines.launch

class EditDailyRoutineFragment : BaseFragment() {

    private var _binding: FragmentEditDailyRoutineBinding? = null
    val binding get() = _binding.acquire()

    private val navController by lazy { findNavController() }

    val viewModel by viewModels<EditDailyRoutineViewModel>()

    private val headerEvent: (View) -> Unit = {
        when (it.id) {
            R.id.header_daily_routine_name -> {
                navController.navigate(
                    R.id.action_editDailyRoutineFragment_to_dailyRoutineNameDialog
                )
            }
            R.id.header_start_date -> {
                configureDatePicker()
            }
            R.id.header_course_duration -> {
                navController.navigate(
                    R.id.action_editDailyRoutineFragment_to_courseDurationDialog
                )
            }
        }
    }

    private val bodyEvent: (View, Int) -> Unit = { view, index ->
        when (view.id) {
            R.id.cell_section_root -> {
                configureTimePicker(true, index)
            }
            R.id.cell_section_delete_button -> {
                viewModel.deleteSectionByIndex(index)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDailyRoutineBinding.inflate(
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
                viewModel.dailyCourses = bundle.getInt(KEY_DAILY_COURSE_COUNT)
                bundle.getParcelable<DailyRoutine>(KEY_DAILY_ROUTINE)?.also { dailyRoutine ->
                    viewModel.dailyRoutineId = dailyRoutine.dailyRoutineId
                    viewModel.parentScheduleId = dailyRoutine.parentScheduleId
                    viewModel.refreshName(dailyRoutine.name)
                    viewModel.refreshDate(dailyRoutine.startDate)
                    if (dailyRoutine.routines.isNotEmpty()) {
                        val duration = dailyRoutine.routines[0].duration / 60
                        viewModel.refreshDuration(duration)
                    }
                    viewModel.refreshRoutines(dailyRoutine.routines)
                }
            }
        }
        binding.fragmentEditDailyRoutineToolbar.setOnMenuItemClickListener { menuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.edit_daily_routine_toolbar_check -> {
                    lifecycleScope.launch {
                        val condition = viewModel.submitDailyRoutine()
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
        binding.fragmentEditDailyRoutineToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        val headerAdapter = EditDailyRoutineHeaderAdapter(headerEvent)
        val bodyAdapter = EditDailyRoutineAdapter(bodyEvent)
        val concatAdapter = ConcatAdapter(headerAdapter, bodyAdapter)
        binding.fragmentEditDailyRoutineRecyclerView.adapter = concatAdapter
        binding.fragmentEditDailyRoutineRecyclerView.layoutManager = LinearLayoutManager(
            context
        ).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.fragmentEditDailyRoutineBottomButton.setOnClickListener {
            configureTimePicker(false)
        }
        viewModel.liveEditSectionList.observe(viewLifecycleOwner) {
            headerAdapter.refreshNode(it.size, viewModel.dailyCourses)
            bodyAdapter.submitList(it)
        }
        viewModel.liveEditName.observe(viewLifecycleOwner) {
            headerAdapter.refreshName(it)
        }
        viewModel.liveEditDate.observe(viewLifecycleOwner) {
            headerAdapter.refreshDate(it)
        }
        viewModel.liveEditDuration.observe(viewLifecycleOwner) {
            headerAdapter.refreshDuration(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureDatePicker() {
        val datePicker = DatePicker(requireActivity())
        datePicker.wheelLayout.setRange(
            DateEntity.target(1970, 1, 1),
            DateEntity.target(3000, 12, 31),
            DateEntity.today()
        )
        datePicker.setOnDatePickedListener { year, month, day ->
            viewModel.refreshDate(Date(year, month, day))
        }
        val observer = object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                if (datePicker.isShowing) {
                    datePicker.dismiss()
                }
                owner.lifecycle.removeObserver(this)
            }
        }
        viewLifecycleOwner.lifecycle.addObserver(observer)
        datePicker.show()
    }

    private fun configureTimePicker(isModify: Boolean, index: Int = -1) {
        val timePicker = TimePicker(requireActivity())
        timePicker.wheelLayout.setTimeMode(TimeMode.HOUR_24_NO_SECOND)
        if (isModify) {
            timePicker.setOnTimePickedListener { hour, minute, second ->
                viewModel.updateSectionByIndex(index, Time(hour, minute, second))
            }
        } else {
            timePicker.setOnTimePickedListener { hour, minute, second ->
                viewModel.insertSectionByTime(Time(hour, minute, second))
            }
        }
        val observer = object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                if (timePicker.isShowing) {
                    timePicker.dismiss()
                }
                owner.lifecycle.removeObserver(this)
            }
        }
        viewLifecycleOwner.lifecycle.addObserver(observer)
        timePicker.show()
    }

    companion object {

        const val KEY_DAILY_ROUTINE = "DAILY_ROUTINE"
        const val KEY_DAILY_COURSE_COUNT = "DAILY_COURSE_COUNT"
    }
}