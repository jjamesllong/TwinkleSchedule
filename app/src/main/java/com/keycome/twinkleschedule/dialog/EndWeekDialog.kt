package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class EndWeekDialog : EditTextDialog() {

    override fun configure() {

        title = "学期总周数"

        confirm = "确认"

        inputType = INPUT_TYPE_NUMBER

        onConfirm {
            lifecycleScope.launch {
                if (editText.isNotBlank()) {
                    Pipette.forInt.emit(
                        Drop(
                            "",
                            editText.toString().toInt()
                        )
                    )
                }
                dismiss()
            }
        }

        onCancel { dismiss() }
    }
}