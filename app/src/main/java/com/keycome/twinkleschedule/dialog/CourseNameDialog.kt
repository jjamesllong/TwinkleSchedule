package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_COURSE_TITLE
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class CourseNameDialog : EditTextDialog() {

    override fun configure() {

        title = getString(R.string.title_course_name_dialog)

        hint = getString(R.string.dialog_course_name_hint)

        confirm = getString(R.string.confirm)

        onCancel { dismiss() }

        onConfirm {
            if (editText.length > binding.dialogEditTextField.counterMaxLength) {
                return@onConfirm
            }
            lifecycleScope.launch {
                Pipette.forString.distribute(KEY_COURSE_TITLE) { editText.toString() }
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