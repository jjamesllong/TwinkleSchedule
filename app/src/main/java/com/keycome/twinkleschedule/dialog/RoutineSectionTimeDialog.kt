package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_SECTION_INDEX
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_SECTION_START_TIME
import com.keycome.twinkleschedule.util.dialogs.TimePickerDialog
import kotlinx.coroutines.launch

class RoutineSectionTimeDialog : TimePickerDialog() {

    override fun configure() {
        timeRange()

        onCancel { dismiss() }

        onConfirm {
            selectedTime?.also { time ->
                val index = arguments?.get(KEY_ROUTINE_SECTION_INDEX)?.let { it as? Int }
                index?.also {
                    lifecycleScope.launch {
                        Pipette.forString.distribute(KEY_ROUTINE_SECTION_START_TIME) {
                            "$time@$index"
                        }
                        dismiss()
                    }
                } ?: dismiss()
            }
        }
    }
}