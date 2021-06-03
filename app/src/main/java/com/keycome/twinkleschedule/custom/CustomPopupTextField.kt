package com.keycome.twinkleschedule.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.PopupTextFieldBinding

class CustomPopupTextField(context: Context) : Dialog(context, R.style.CustomPopupTextField) {

    lateinit var binding: PopupTextFieldBinding
    private var positiveAction: PositiveAction? = null
    private var negativeAction: NegativeAction? = null
    var autoDismiss: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PopupTextFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        initEvent()
    }

    private fun initEvent() {
        binding.popupTextFieldPositiveButton.setOnClickListener {
            positiveAction?.action()
            if (autoDismiss && isShowing) dismiss()
        }

        binding.popupTextFieldNegativeButton.setOnClickListener {
            negativeAction?.action()
            if (autoDismiss && isShowing) dismiss()
        }
    }

    fun onPositiveButtonPressed(action: PositiveAction) {
        this.positiveAction = action
    }

    fun onNegativeButtonPressed(action: NegativeAction) {
        this.negativeAction = action
    }

    fun interface PositiveAction {
        fun action()
    }

    fun interface NegativeAction {
        fun action()
    }
}