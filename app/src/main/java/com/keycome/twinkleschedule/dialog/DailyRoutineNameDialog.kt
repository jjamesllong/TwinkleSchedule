package com.keycome.twinkleschedule.dialog

import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.base.EditTextDialog
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.model.EditDailyRoutineViewModel
import kotlinx.coroutines.launch

class DailyRoutineNameDialog : EditTextDialog() {

    override fun configure() {

        title = "作息表名称"

        confirm = "确定"

        onCancel { dismiss() }

        onConfirm {
            val text = editText.toString()
            if (text.isNotBlank()) {
                lifecycleScope.launch {
                    Pipette.pipetteForString.emit(
                        Drop(EditDailyRoutineViewModel.dailyRoutineName, text)
                    )
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }
}