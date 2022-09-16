package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_WEEK
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class ScheduleEndWeek : EditTextDialog() {

    override fun configure() {

        title = getString(R.string.title_schedule_end_week_dialog)

        confirm = getString(R.string.confirm)

        inputType = INPUT_TYPE_NUMBER

        onConfirm {
            val endWeek = editText.toString()

            if (endWeek.isNotBlank()) {
                lifecycleScope.launch {
                    Pipette.forInt.distribute(KEY_SCHEDULE_END_WEEK) {
                        endWeek.toInt()
                    }
                    dismiss()
                }
            } else {
                dismiss()
            }
        }

        onCancel { dismiss() }
    }
}