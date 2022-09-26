package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_COURSE_CLASSROOM
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class ClassroomDialog : EditTextDialog() {

    override fun configure() {

        title = getString(R.string.course_classroom)

        hint = getString(R.string.course_classroom)

        onCancel { dismiss() }

        onConfirm {
            if (editText.length > binding.dialogEditTextField.counterMaxLength) {
                return@onConfirm
            }
            lifecycleScope.launch {
                Pipette.forString.distribute(KEY_COURSE_CLASSROOM) { editText.toString() }
                dismiss()
            }
        }

        textWatcher {
            afterTextChanged {
                val l = it?.length ?: 0
                if (l > binding.dialogEditTextField.counterMaxLength) {
                    binding.dialogEditTextField.error = getString(
                        R.string.dialog_course_name_text_overflow
                    )
                } else {
                    binding.dialogEditTextField.error = null
                }
            }
        }
    }
}