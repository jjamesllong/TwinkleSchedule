package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_COURSE_TEACHER
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class TeacherNameDialog : EditTextDialog() {

    override fun configure() {

        title = getString(R.string.course_teacher)

        hint = getString(R.string.course_teacher)

        onCancel { dismiss() }

        onConfirm {
            if (editText.length > binding.editTextDialogField.counterMaxLength) {
                return@onConfirm
            }
            lifecycleScope.launch {
                Pipette.forString.distribute(KEY_COURSE_TEACHER) { editText.toString() }
                dismiss()
            }
        }

        textWatcher {
            afterTextChanged {
                val l = it?.length ?: 0
                if (l > binding.editTextDialogField.counterMaxLength) {
                    binding.editTextDialogField.error = getString(
                        R.string.dialog_course_name_text_overflow
                    )
                } else {
                    binding.editTextDialogField.error = null
                }
            }
        }
    }
}