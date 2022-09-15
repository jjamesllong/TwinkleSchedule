package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_NAME
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class ScheduleNameDialog : EditTextDialog() {

    override fun configure() {

        title = getString(R.string.title_schedule_name_dialog)

        onCancel { dismiss() }

        onConfirm {
            val name = editText.toString()
            if (name.isNotBlank()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    Pipette.forString.distribute(KEY_SCHEDULE_NAME) { name }
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }
}