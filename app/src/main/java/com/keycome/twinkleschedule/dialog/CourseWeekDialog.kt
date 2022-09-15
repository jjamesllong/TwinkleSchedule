package com.keycome.twinkleschedule.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseWeekBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_WEEK
import com.keycome.twinkleschedule.util.slidingtrigger.CellState
import com.keycome.twinkleschedule.util.slidingtrigger.toCellStateWithSize
import com.keycome.twinkleschedule.util.slidingtrigger.toSlidingTrigger
import kotlinx.coroutines.launch

@SuppressLint("ClickableViewAccessibility")
class CourseWeekDialog : BaseDialogFragment() {

    private var _binding: DialogCourseWeekBinding? = null
    val binding get() = _binding.acquire()

    private var scheduleEndWeek = 0
    private var selectedWeeks = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogCourseWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleEndWeek = savedInstanceState?.getInt(
            KEY_SCHEDULE_END_WEEK
        ) ?: arguments?.getInt(
            KEY_SCHEDULE_END_WEEK
        ) ?: 0

        selectedWeeks = savedInstanceState?.getInt(
            KEY_SELECTED_COURSE_WEEKS
        ) ?: arguments?.getInt(
            KEY_SELECTED_COURSE_WEEKS
        ) ?: 0

        val iconSize = 38.dp

        val childrenList = (1..scheduleEndWeek).map { i ->
            TextView(context).apply {
                id = View.generateViewId()
                layoutParams = ConstraintLayout.LayoutParams(iconSize, iconSize)
                setBackgroundResource(R.drawable.bg_selector_icon_selectable)
                gravity = Gravity.CENTER
                text = i.toString()
            }.also { binding.dialogCourseWeekContainer.addView(it) }
        }

        binding.dialogCourseWeekFlow.referencedIds = childrenList.map { it.id }.toIntArray()

        val slidingTrigger = binding.dialogCourseWeekContainer.toSlidingTrigger(
            childrenList
        ) { size, state ->
            selectedWeeks = state
            when (state.toCellStateWithSize(size)) {
                CellState.None -> {
                    binding.dialogCourseWeekAll.isSelected = false
                    binding.dialogCourseWeekEven.isSelected = false
                    binding.dialogCourseWeekOdd.isSelected = false
                    binding.dialogCourseWeekAll.text = getString(
                        R.string.dialog_course_week_select_all
                    )
                }
                CellState.All -> {
                    binding.dialogCourseWeekAll.isSelected = true
                    binding.dialogCourseWeekEven.isSelected = false
                    binding.dialogCourseWeekOdd.isSelected = false
                    binding.dialogCourseWeekAll.text = getString(
                        R.string.dialog_course_week_select_none
                    )
                }
                CellState.Even -> {
                    binding.dialogCourseWeekAll.isSelected = false
                    binding.dialogCourseWeekEven.isSelected = true
                    binding.dialogCourseWeekOdd.isSelected = false
                    binding.dialogCourseWeekAll.text = getString(
                        R.string.dialog_course_week_select_all
                    )
                }
                CellState.Odd -> {
                    binding.dialogCourseWeekAll.isSelected = false
                    binding.dialogCourseWeekEven.isSelected = false
                    binding.dialogCourseWeekOdd.isSelected = true
                    binding.dialogCourseWeekAll.text = getString(
                        R.string.dialog_course_week_select_all
                    )
                }
                CellState.Other -> {
                    binding.dialogCourseWeekAll.isSelected = false
                    binding.dialogCourseWeekEven.isSelected = false
                    binding.dialogCourseWeekOdd.isSelected = false
                    binding.dialogCourseWeekAll.text = getString(
                        R.string.dialog_course_week_select_all
                    )
                }
            }
        }

        slidingTrigger.setCellState(selectedWeeks)

        binding.dialogCourseWeekAll.setOnClickListener {
            if (it.isSelected) {
                slidingTrigger.setCellStateNon()
            } else {
                slidingTrigger.setCellStateAll()
            }
        }
        binding.dialogCourseWeekEven.setOnClickListener {
            if (it.isSelected) {
                slidingTrigger.setCellStateNon()
            } else {
                slidingTrigger.setCellStateEven()
            }
        }
        binding.dialogCourseWeekOdd.setOnClickListener {
            if (it.isSelected) {
                slidingTrigger.setCellStateNon()
            } else {
                slidingTrigger.setCellSateOdd()
            }
        }
        binding.dialogCourseWeekCancel.setOnClickListener { dismiss() }
        binding.dialogCourseWeekConfirm.setOnClickListener {
            lifecycleScope.launch {
                Pipette.forInt.distribute(KEY_SELECTED_COURSE_WEEKS) { selectedWeeks }
                dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SCHEDULE_END_WEEK, scheduleEndWeek)
        outState.putInt(KEY_SELECTED_COURSE_WEEKS, selectedWeeks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    companion object {

        const val KEY_SELECTED_COURSE_WEEKS = "selected_course_weeks"
    }
}