package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_START_DATE
import com.keycome.twinkleschedule.util.dialogs.DatePickerDialog
import kotlinx.coroutines.launch

class RoutineStartDateDialog : DatePickerDialog() {

    override fun configure() {
        dateRange()
        onCancel { dismiss() }
        onConfirm {
            selectedDate?.also { date ->
                lifecycleScope.launch {
                    Pipette.forString.distribute(KEY_ROUTINE_START_DATE) {
                        date.toString()
                    }
                    dismiss()
                }
            }
        }
    }
}