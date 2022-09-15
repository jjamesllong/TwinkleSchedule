package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_SECTION
import com.keycome.twinkleschedule.util.dialogs.EditTextDialog
import kotlinx.coroutines.launch

class EndSectionDialog : EditTextDialog() {

    override fun configure() {

        title = getString(R.string.title_end_section_dialog)

        inputType = INPUT_TYPE_NUMBER

        onCancel { dismiss() }

        onConfirm {
            val dailyCourses = editText.toString().toInt()
            viewLifecycleOwner.lifecycleScope.launch {
                Pipette.forInt.distribute(KEY_SCHEDULE_END_SECTION) { dailyCourses }
                dismiss()
            }
        }
    }
}