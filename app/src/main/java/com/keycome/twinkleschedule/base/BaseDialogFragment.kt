package com.keycome.twinkleschedule.base

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    @StyleRes
    private var animationResId: Int = nullStyleRes

    private var gravity: Int = Center

    private var fullWidth: Boolean = false

    private var hasAnimation: Boolean = true

    private var animationRemoved: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BaseDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            animationRemoved = true
        } else {
            if (hasAnimation) {
                @StyleRes val id = considerAnimationResId(gravity)
                if (id != nullStyleRes) {
                    dialog?.window?.setWindowAnimations(id)
                }
            }
        }
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                if (!hasAnimation) return
                if (animationRemoved) {
                    lifecycleScope.launch {
                        delay(300)
                        dialog?.window?.setWindowAnimations(considerAnimationResId(gravity))
                    }
                }
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                if (!hasAnimation) return
                dialog?.window?.setWindowAnimations(nullStyleRes)
                animationRemoved = true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (gravity == Bottom) {
            dialog?.window?.setGravity(Gravity.BOTTOM)
        }
        if (fullWidth) {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    fun requestFullScreenBottomDialog() {
        hasAnimation = true
        gravity = Bottom
        fullWidth = true
    }

    @StyleRes
    fun considerAnimationResId(gravity: Int): Int {
        return when (gravity) {
            Center -> R.style.FloatingCenterDialogAnimation
            Bottom -> R.style.FullScreenBottomDialogAnimation
            else -> nullStyleRes
        }
    }

    companion object {

        @StyleRes
        const val nullStyleRes = 0

        const val NoGravity = -1
        const val Center = 0
        const val Bottom = 1
    }

}