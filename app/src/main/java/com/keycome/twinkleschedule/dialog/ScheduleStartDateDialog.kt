package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_START_DATE
import com.keycome.twinkleschedule.util.dialogs.DatePickerDialog
import kotlinx.coroutines.launch

class ScheduleStartDateDialog : DatePickerDialog() {

    override fun configure() {
        dateRange()
        onCancel { dismiss() }
        onConfirm {
            selectedDate?.also { date ->
                lifecycleScope.launch {
                    Pipette.forString.distribute(KEY_SCHEDULE_START_DATE) {
                        date.toHyphenDateString()
                    }
                    dismiss()
                }
            }
        }
    }
}