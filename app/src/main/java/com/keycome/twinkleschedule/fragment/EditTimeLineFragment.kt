package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.TimePicker
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.EditTimeLineAdapter
import com.keycome.twinkleschedule.adapter.EditTimeLineHeaderAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentEditTimeLineBinding
import com.keycome.twinkleschedule.model.EditTimeLineViewModel
import com.keycome.twinkleschedule.record.sketch.TimeLine
import com.keycome.twinkleschedule.record.span.Date
import com.keycome.twinkleschedule.record.span.Time

class EditTimeLineFragment : BaseFragment() {

    private var _binding: FragmentEditTimeLineBinding? = null
    val binding get() = _binding.acquire()

    private val navController by lazy { findNavController() }

    val viewModel by viewModels<EditTimeLineViewModel>()

    private val headerEvent: (View) -> Unit = {
        when (it.id) {
            R.id.header_current_time_line -> {
                navController.navigate(R.id.action_editTimeLineFragment2_to_timeLineNameDialog)
            }
            R.id.header_start_date -> {
                configureDatePicker()
            }
        }
    }

    private val bodyEvent: (View, Int) -> Unit = { view, index ->
        when (view.id) {
            R.id.cell_add_time_line_root -> {
                configureTimePicker(true, index)
            }
            R.id.cell_add_time_line_delete_button -> {
                viewModel.deleteTimeByIndex(index)
            }
        }
    }

    private val headerAdapter = EditTimeLineHeaderAdapter(headerEvent)
    private val bodyAdapter = EditTimeLineAdapter(bodyEvent)
    private val concatAdapter = ConcatAdapter(headerAdapter, bodyAdapter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTimeLineBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.firstIn) {
            arguments?.let { bundle ->
                viewModel.dailyCourses = bundle.getInt(bundleDailyCourses)
                bundle.getParcelable<TimeLine>(bundleTimeLine)?.let { timeLine ->
                    viewModel.timeLineId = timeLine.id
                    viewModel.refreshName(timeLine.name)
                    viewModel.refreshDate(timeLine.startDate)
                    viewModel.refreshTimeList(timeLine.timeList.toMutableList())
                }
            }
            viewModel.firstIn = false
        }
        binding.fragmentAddTimeLineToolbar.setOnMenuItemClickListener { menuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.edit_time_line_toolbar_check -> {
                    if (viewModel.checkTimeLineRight()) {
                        viewModel.submitTimeLine()
                        navController.navigateUp()
                    } else {
                        Toast.makeText(context, "错误", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
        binding.fragmentAddTimeLineToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        binding.fragmentAddTimeLineRecyclerView.adapter = concatAdapter
        binding.fragmentAddTimeLineRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.fragmentAddTimeLineBottomButton.setOnClickListener {
            configureTimePicker(false)
        }
        viewModel.liveEditingTimeList.observe(viewLifecycleOwner) {
            bodyAdapter.submitList(ArrayList(it))
        }
        viewModel.liveEditingName.observe(viewLifecycleOwner) {
            headerAdapter.refreshName(it)
        }
        viewModel.liveEditingDate.observe(viewLifecycleOwner) {
            headerAdapter.refreshDate(it)
        }
        viewModel.liveTimeNode.observe(viewLifecycleOwner) {
            headerAdapter.refreshNode(it, viewModel.dailyCourses)
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
                viewModel.updateTimeByIndex(index, Time(hour, minute, second))
            }
        } else {
            timePicker.setOnTimePickedListener { hour, minute, second ->
                viewModel.insertTimeByTime(Time(hour, minute, second))
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
        const val bundleTimeLine = "fragment_edit_time_line_argument_time_line"
        const val bundleDailyCourses = "fragment_edit_time_line_argument_daily_courses"
    }
}