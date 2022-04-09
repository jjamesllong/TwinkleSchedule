package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseDayBinding
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.extension.acquire
import kotlinx.coroutines.launch

class CourseDayDialog : BaseDialogFragment() {

    private var _binding: DialogCourseDayBinding? = null
    val binding get() = _binding.acquire()

    private var previousSelectedDay = 0

    private val dayInItem by lazy { arguments?.getInt(DayInItem) ?: 0 }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogCourseDayBinding.inflate(layoutInflater, container, false)
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
        if (savedInstanceState == null) {
            if (dayInItem != 0) {
                dayList[dayInItem - 1].isSelected = true
                previousSelectedDay = dayInItem
            }
        } else {
            val day = savedInstanceState.getInt(PreviousSelectedDay)
            if (day != 0) {
                dayList[day - 1].isSelected = true
                previousSelectedDay = day
            }
        }
        for (i in dayList.indices) {
            dayList[i].setOnClickListener {
                it.isSelected = true
                if (previousSelectedDay != 0) {
                    dayList[previousSelectedDay - 1].isSelected = false
                }
                previousSelectedDay = i + 1
            }
        }
        binding.dialogCourseDayCancel.setOnClickListener { dismiss() }
        binding.dialogCourseDayConfirm.setOnClickListener {
            if (previousSelectedDay != 0) {
                lifecycleScope.launch {
                    Pipette.pipetteForInt.emit(Drop(DayInItem, previousSelectedDay))
                    dismiss()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PreviousSelectedDay, previousSelectedDay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val PreviousSelectedDay = "saved_previous_selected_day"
        const val DayInItem = "day_in_item"
    }
}