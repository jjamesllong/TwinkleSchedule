package com.keycome.twinkleschedule.dialog

import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.base.EditTextDialog
import com.keycome.twinkleschedule.model.EditTimeLineViewModel

class TimeLineNameDialog : EditTextDialog() {

    val viewModel by viewModels<TimeLineNameViewModel>()

    override fun onConfigure() {

        title = "作息表名称"

        confirmHint = "确定"

        onCancel { dismiss() }

        onConfirm {
            val text = binding.editTextDialogField.text.toString()
            if (text.isNotBlank()) {
                viewModel.liveEditingName?.value = text
            }
            dismiss()
        }
    }

    class TimeLineNameViewModel : BaseViewModel() {

        val liveEditingName by shareOnlyVariable<MutableLiveData<String>>(
            EditTimeLineViewModel.timeLineName
        )

        override fun onRemove() {
            super.onRemove()
            release(EditTimeLineViewModel.timeLineName)
        }
    }
}