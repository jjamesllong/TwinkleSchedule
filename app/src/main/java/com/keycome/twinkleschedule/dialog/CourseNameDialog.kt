package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.base.EditTextDialog
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import kotlinx.coroutines.launch

class CourseNameDialog : EditTextDialog() {

    override fun configure() {

        title = "名称"

        confirm = "确认"

        onCancel { dismiss() }

        onConfirm {
            lifecycleScope.launch {
                Pipette.pipetteForString.emit(Drop("edit_course_name", editText.toString()))
                dismiss()
            }
        }
    }
}