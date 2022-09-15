package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseDayBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.util.const.KEY_COURSE_DAY
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_DAY
import kotlinx.coroutines.launch

class CourseDayDialog : BaseDialogFragment() {

    private var _binding: DialogCourseDayBinding? = null
    val binding get() = _binding.acquire()

    private var endDay = 0

    private var selectedDay = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogCourseDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dayList = listOf(
            binding.dialogTextMonday,
            binding.dialogTextTuesday,
            binding.dialogTextWednesday,
            binding.dialogTextThursday,
            binding.dialogTextFriday,
            binding.dialogTextSaturday,
            binding.dialogTextSunday
        )
        endDay = savedInstanceState?.getInt(
            KEY_SCHEDULE_END_DAY
        ) ?: arguments?.getInt(
            KEY_SCHEDULE_END_DAY
        ) ?: 0
        val s = savedInstanceState?.getInt(
            KEY_COURSE_DAY
        ) ?: arguments?.getInt(
            KEY_COURSE_DAY
        ) ?: 0
        setDaySelected(s, dayList)
        for (i in dayList.indices) {
            if (i < endDay) {
                dayList[i].setOnClickListener {
                    setDaySelected(i + 1, dayList)
                }
            } else {
                dayList[i].visibility = View.INVISIBLE
            }
        }
        binding.dialogCourseDayCancel.setOnClickListener { dismiss() }
        binding.dialogCourseDayConfirm.setOnClickListener {
            if (selectedDay != 0) {
                lifecycleScope.launch {
                    Pipette.forInt.distribute(KEY_COURSE_DAY) { selectedDay }
                    dismiss()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SCHEDULE_END_DAY, endDay)
        outState.putInt(KEY_COURSE_DAY, selectedDay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDaySelected(day: Int, list: List<TextView>) {
        val s = selectedDay
        if (s == day) return
        if (s != 0) {
            list[s - 1].isSelected = false
        }
        if (day != 0) {
            list[day - 1].isSelected = true
        }
        selectedDay = day
    }
}