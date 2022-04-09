package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.base.EditTextDialog
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import kotlinx.coroutines.launch

class TeacherNameDialog : EditTextDialog() {

    override fun configure() {
        title = "教师"
        confirm = "确认"
        onCancel { dismiss() }
        onConfirm {
            lifecycleScope.launch {
                Pipette.pipetteForString.emit(Drop(TEACHER_NAME_TAG, editText.toString()))
                dismiss()
            }
        }
    }

    companion object {

        const val TEACHER_NAME_TAG = "teacher_name_tag"
    }
}