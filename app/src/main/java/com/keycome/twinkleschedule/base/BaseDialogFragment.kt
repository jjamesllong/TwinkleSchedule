package com.keycome.twinkleschedule.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.extension.fragments.activityViewModelDelegate
import com.keycome.twinkleschedule.extension.fragments.viewModelDelegate

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AppCompatDialog(context)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)

            when (getDialogGravity()) {
                GRAVITY_CENTER -> setGravity(Gravity.CENTER)
                GRAVITY_BOTTOM -> setGravity(Gravity.BOTTOM)
                else -> setGravity(Gravity.CENTER)
            }

            setWindowAnimations(getDialogAnimations())
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configDialogSize(getDialogMode())
    }

    private fun configDialogSize(mode: Int) {
        when (mode) {
            MODE_WRAP -> dialog?.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            MODE_FILL -> dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            else -> dialog?.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

    }

    protected inline fun <reified VM : BaseViewModel> viewModels(): Lazy<VM> {
        return viewModelDelegate()
    }

    protected inline fun <reified VM : BaseViewModel> activityViewModels(): Lazy<VM> {
        return activityViewModelDelegate()
    }

    open fun getDialogGravity(): Int {
        return GRAVITY_CENTER
    }

    open fun getDialogMode(): Int {
        return MODE_WRAP
    }

    open fun getDialogAnimations(): Int {
        return R.style.FloatingCenterDialogAnimation
    }


    companion object {

        const val GRAVITY_CENTER = 0
        const val GRAVITY_BOTTOM = 1

        const val MODE_WRAP = 0
        const val MODE_FILL = 1
    }

}