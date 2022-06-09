package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.base.EditTextDialog
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import kotlinx.coroutines.launch

class ClassroomDialog : EditTextDialog() {

    override fun configure() {
        title = "教室"
        confirm = "确认"
        onCancel { dismiss() }
        onConfirm {
            lifecycleScope.launch {
                Pipette.forString.emit(Drop(CLASSROOM_TAG, editText.toString()))
                dismiss()
            }
        }
    }

    companion object {
        const val CLASSROOM_TAG = "classroom_tag"
    }
}