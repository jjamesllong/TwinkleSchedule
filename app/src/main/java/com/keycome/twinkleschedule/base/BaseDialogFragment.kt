package com.keycome.twinkleschedule.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.extension.fragments.activityViewModelDelegate
import com.keycome.twinkleschedule.extension.fragments.viewModelDelegate

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    private var firstPresent = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AppCompatDialog(context)
        dialog.window?.also {
            it.setBackgroundDrawable(null)
            it.requestFeature(Window.FEATURE_NO_TITLE)
            it.setWindowAnimations(0)
            when (getDialogGravity()) {
                GRAVITY_CENTER -> it.setGravity(Gravity.CENTER)
                GRAVITY_BOTTOM -> it.setGravity(Gravity.BOTTOM)
                else -> {}
            }
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        when (getDialogMode()) {
            MODE_WRAP -> {}
            MODE_FILL -> dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            else -> {}
        }
        considerPlayAnimation()
    }

    protected inline fun <reified VM : BaseViewModel> viewModels(): Lazy<VM> {
        return viewModelDelegate()
    }

    protected inline fun <reified VM : BaseViewModel> activityViewModels(): Lazy<VM> {
        return activityViewModelDelegate()
    }

    private fun considerPlayAnimation() {
        if (firstPresent) {
            firstPresent = false
            dialog?.window?.decorView?.also {
                val animationId = getDialogAnimation().let { id ->
                    if (id == 0) R.anim.scale_expand_with_alpha else id
                }
                val animation = AnimationUtils.loadAnimation(
                    context,
                    animationId
                ) as AnimationSet
                it.startAnimation(animation)
            }
        }
    }

    open fun getDialogGravity(): Int {
        return GRAVITY_CENTER
    }

    open fun getDialogMode(): Int {
        return MODE_WRAP
    }

    open fun getDialogAnimation(): Int {
        return 0
    }


    companion object {

        const val GRAVITY_CENTER = 0
        const val GRAVITY_BOTTOM = 1

        const val MODE_WRAP = 0
        const val MODE_FILL = 1
    }

}