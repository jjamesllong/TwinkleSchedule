package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.CheckDailyRoutineAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.model.EditScheduleViewModel
import com.keycome.twinkleschedule.record.interval.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditScheduleFragment : BaseFragment() {

    private var _binding: FragmentEditScheduleBinding? = null
    val binding get() = _binding!!

    private val navController: NavController by lazy { findNavController() }

    val viewModel by viewModels<EditScheduleViewModel>()

    private val editScheduleId by lazy { arguments?.getLong(KEY_EDIT_SCHEDULE_ID) ?: 0L }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditScheduleBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentEditScheduleToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        binding.editScheduleNameItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_scheduleNameDialog)
        }
        binding.editSchoolOpeningDateItem.setOnClickListener {
            configureDatePicker()
        }
        binding.editDailyCoursesItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_dailyCoursesDialog)
        }
        binding.editEndDayItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_endDayDialog)
        }
        binding.editWeeksItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_endWeekDialog)
        }
        binding.addNewDailyRoutineText.setOnClickListener {
            navController.navigate(
                R.id.action_editScheduleFragment_to_editDailyRoutineFragment,
                Bundle().apply {
                    putInt(
                        EditDailyRoutineFragment.KEY_DAILY_COURSE_COUNT,
                        viewModel.liveDailyCourses.value ?: 0
                    )
                    putParcelable(
                        EditDailyRoutineFragment.KEY_DAILY_ROUTINE,
                        viewModel.requestDailyRoutine()
                    )
                }
            )
        }
        binding.editScheduleSubmitButton.setOnClickListener {
            it.isEnabled = false
            if (viewModel.checkScheduleRight()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.insertSchedule(binding.toDisplayCheckBox.isChecked)
                    withContext(Dispatchers.Main) {
                        navController.navigate(
                            R.id.action_editScheduleFragment_to_displayCoursesFragment
                        )
                    }
                }
            } else {
                it.isEnabled = true
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
            }
        }
        val dailyRoutineAdapter = CheckDailyRoutineAdapter { v, dailyRoutine ->
            when (v.id) {
                R.id.daily_routine_root -> {
                    navController.navigate(
                        R.id.action_editScheduleFragment_to_editDailyRoutineFragment,
                        Bundle().apply {
                            putInt(
                                EditDailyRoutineFragment.KEY_DAILY_COURSE_COUNT,
                                viewModel.liveDailyCourses.value ?: 0
                            )
                            putParcelable(EditDailyRoutineFragment.KEY_DAILY_ROUTINE, dailyRoutine)
                        }
                    )
                }
                R.id.daily_routine_delete -> {
                    if (!viewModel.deleteDailyRoutine(dailyRoutine.dailyRoutineId)) {
                        Toast.makeText(
                            context,
                            getString(R.string.error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        binding.dailyRoutinesRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.dailyRoutinesRecyclerView.adapter = dailyRoutineAdapter
        viewModel.liveScheduleName.observe(viewLifecycleOwner) {
            binding.scheduleNameText.text = it
        }
        viewModel.liveSchoolOpeningDate.observe(viewLifecycleOwner) {
            binding.schoolOpeningDateText.text = it.toDotDateString()
        }
        viewModel.liveDailyCourses.observe(viewLifecycleOwner) {
            binding.dailyCoursesText.text = it.toString()
        }
        viewModel.liveEndDay.observe(viewLifecycleOwner) {
            binding.editEndDayText.text = it.name
        }
        viewModel.liveWeeks.observe(viewLifecycleOwner) {
            binding.weeksText.text = it.toString()
        }
        viewModel.liveDailyRoutines.observe(viewLifecycleOwner) {
            dailyRoutineAdapter.submitList(it)
        }
        viewModel.onFirstPresent {
            if (editScheduleId != 0L) {
                viewModel.requestModifiedDataById(editScheduleId)
            }
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
            viewModel.refreshSchoolOpeningDate(Date(year, month, day))
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

    companion object {

        const val KEY_EDIT_SCHEDULE_ID = "EDIT_SCHEDULE_ID"
    }
}