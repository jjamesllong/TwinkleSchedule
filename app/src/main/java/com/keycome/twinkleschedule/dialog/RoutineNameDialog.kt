package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_NAME
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class RoutineNameDialog : EditTextDialog() {

    override fun configure() {

        title = getString(R.string.fragment_edit_routine_name)

        confirm = getString(R.string.confirm)

        onCancel { dismiss() }

        onConfirm {
            val text = editText.toString()
            if (text.isNotBlank()) {
                lifecycleScope.launch {
                    Pipette.forString.distribute(KEY_ROUTINE_NAME) { text }
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }
}