package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogEndDayBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_DAY
import kotlinx.coroutines.launch

class EndDayDialog : BaseDialogFragment() {

    private var _binding: DialogEndDayBinding? = null
    val binding get() = _binding!!

    private var selectedDay = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogEndDayBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDay = savedInstanceState?.getInt(
            KEY_SCHEDULE_END_DAY
        ) ?: arguments?.getInt(
            KEY_SCHEDULE_END_DAY
        ) ?: -1

        val checkId = when (selectedDay) {
            5 -> R.id.dialog_end_day_friday
            6 -> R.id.dialog_end_day_saturday
            7 -> R.id.dialog_end_day_sunday
            else -> selectedDay
        }
        binding.dialogEndDayRadioGroup.check(checkId)

        binding.dialogEndDayRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedDay = when (checkedId) {
                R.id.dialog_end_day_friday -> 5
                R.id.dialog_end_day_saturday -> 6
                R.id.dialog_end_day_sunday -> 7
                else -> -1
            }
        }

        binding.dialogEndDayClose.setOnClickListener { dismiss() }

        binding.dialogEndDayConfirm.setOnClickListener {
            if (selectedDay != -1) {
                lifecycleScope.launch {
                    Pipette.forInt.distribute(KEY_SCHEDULE_END_DAY) { selectedDay }
                    dismiss()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SCHEDULE_END_DAY, selectedDay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}