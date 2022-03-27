package com.keycome.twinkleschedule.dialog

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.base.EditTextDialog
import com.keycome.twinkleschedule.model.EditScheduleViewModel

class ScheduleNameDialog : EditTextDialog() {

    val viewModel by viewModels<ScheduleNameViewModel>()

    private var editingText: Editable? = null

    override fun onConfigure() {

        title = "课表名称"

        confirmHint = "确定"

        onCancel { dismiss() }

        onConfirm {
            if (!editingText.isNullOrBlank()) {
                viewModel.liveEditText?.value = editingText.toString()
            }
            dismiss()
        }

        onTextChange(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                editingText = s
            }

        })
    }

    class ScheduleNameViewModel : BaseViewModel() {
        val liveEditText by shareOnlyVariable<MutableLiveData<String>>(
            EditScheduleViewModel.sharedScheduleName
        )

        override fun onRemove() {
            super.onRemove()
            release(EditScheduleViewModel.sharedScheduleName)
        }
    }
}