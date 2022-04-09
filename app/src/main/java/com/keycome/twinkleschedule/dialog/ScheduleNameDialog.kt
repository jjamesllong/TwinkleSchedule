package com.keycome.twinkleschedule.dialog

import android.text.Editable
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.base.EditTextDialog
import com.keycome.twinkleschedule.model.EditScheduleViewModel

class ScheduleNameDialog : EditTextDialog() {

    val viewModel by viewModels<ScheduleNameViewModel>()

    private var editingText: Editable? = null

    override fun configure() {

        title = "课表名称"

        confirm = "确定"

        onCancel { dismiss() }

        onConfirm {
            if (!editingText.isNullOrBlank()) {
                viewModel.liveEditText?.value = editingText.toString()
            }
            dismiss()
        }

        textWatcher {
            afterTextChanged {
                editingText = it
            }
        }
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