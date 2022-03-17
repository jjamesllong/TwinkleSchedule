package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.CheckTimeLineAdapter
import com.keycome.twinkleschedule.adapter.CheckTimeLineFooterAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.model.EditScheduleViewModel
import com.keycome.twinkleschedule.record.sketch.TimeLine
import com.keycome.twinkleschedule.record.span.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditScheduleFragment : BaseFragment() {

    private var _binding: FragmentEditScheduleBinding? = null
    val binding get() = _binding!!

    private val navController: NavController by lazy { findNavController() }

    val viewModel by viewModels<EditScheduleViewModel>()

    private val timeLineEvent: (TimeLine) -> Unit by lazy {
        return@lazy { timeLine: TimeLine ->
            navController.navigate(
                R.id.action_editScheduleFragment_to_editTimeLineFragment,
                Bundle().apply {
                    putParcelable(EditTimeLineFragment.bundleTimeLine, timeLine)
                    viewModel.liveDailyCourses.value?.let { dailyCourses ->
                        putInt(
                            EditTimeLineFragment.bundleDailyCourses,
                            dailyCourses
                        )
                    }
                }
            )
        }
    }

    private val timeLineFooterEvent: (View) -> Unit by lazy {
        return@lazy {
            navController.navigate(
                R.id.action_editScheduleFragment_to_editTimeLineFragment,
                Bundle().apply {
                    putParcelable(
                        EditTimeLineFragment.bundleTimeLine,
                        viewModel.requestNewTimeLine()
                    )
                    viewModel.liveDailyCourses.value?.let { dailyCourses ->
                        putInt(
                            EditTimeLineFragment.bundleDailyCourses,
                            dailyCourses
                        )
                    }
                }
            )
        }
    }

    private val timeLineAdapter by lazy { CheckTimeLineAdapter(timeLineEvent) }
    private val timeLineFooterAdapter by lazy { CheckTimeLineFooterAdapter(timeLineFooterEvent) }
    private val concatAdapter by lazy { ConcatAdapter(timeLineAdapter, timeLineFooterAdapter) }

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
        binding.editSchoolBeginDateItem.setOnClickListener {
            configureDatePicker()
        }
        binding.editDailyCoursesItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_dailyCoursesDialog)
        }
        binding.editEndDayItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_endDayDialog)
        }
        binding.editCourseDurationItem.setOnClickListener {
            navController.navigate(R.id.action_editScheduleFragment_to_courseDurationDialog)
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
                Toast.makeText(context, "错误", Toast.LENGTH_SHORT).show()
            }
        }
        configureRecyclerView()
        viewModel.liveScheduleName.observe(viewLifecycleOwner) {
            binding.scheduleNameText.text = it
        }
        viewModel.liveSchoolBeginDate.observe(viewLifecycleOwner) {
            binding.schoolBeginDateText.text = it.toDotDateString()
        }
        viewModel.liveDailyCourses.observe(viewLifecycleOwner) {
            binding.dailyCoursesText.text = it.toString()
        }
        viewModel.liveEndDay.observe(viewLifecycleOwner) {
            binding.editEndDayText.text = it.name
        }
        viewModel.liveCourseDuration.observe(viewLifecycleOwner) {
            binding.courseDurationText.text = it.toString()
        }
        viewModel.liveTimeLine.observe(viewLifecycleOwner) {
            timeLineAdapter.submitList(it.toList())
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
            viewModel.refreshSchoolBeginDate(Date(year, month, day))
        }
        val observer = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                if (datePicker.isShowing) {
                    datePicker.dismiss()
                }
                owner.lifecycle.removeObserver(this)
            }
        }
        viewLifecycleOwner.lifecycle.addObserver(observer)
        datePicker.show()
    }

    private fun configureRecyclerView() {
        binding.timeLineRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.timeLineRecyclerView.adapter = concatAdapter
    }
}