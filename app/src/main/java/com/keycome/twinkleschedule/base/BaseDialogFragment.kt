package com.keycome.twinkleschedule.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.delivery.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    val companionViewModel by viewModels<BaseFragment.CompanionViewModel>()

    @StyleRes
    private var animationResId: Int = nullStyleRes

    private var gravity: Int = Center

    private var fullWidth: Boolean = false

    private var hasAnimation: Boolean = true

    private var animationRemoved: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        savedInstanceState?.let {
            animationRemoved = true
        } ?: run {
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

    fun requestBottomFullDialog() {
        hasAnimation = true
        gravity = Bottom
        fullWidth = true
    }

    @StyleRes
    fun considerAnimationResId(gravity: Int = NoGravity): Int {
        return when (gravity) {
            Center -> nullStyleRes
            Bottom -> R.style.BottomFullDialogAnimation
            else -> nullStyleRes
        }
    }

    inline fun emit(direction: String, action: PutBoundScope.() -> Unit) {
        if (direction.isBlank()) {
            throw Exception()
        }
        action(PutBoundScopeImpl(direction))
    }

    inline fun retrieve(direction: String, action: GetBoundScope.() -> Unit) {
        if (direction.isBlank()) {
            throw Exception()
        }
        companionViewModel.addDirection(direction)
        action(GetBoundScopeImpl(direction))
    }

    inline fun retrieveOnce(direction: String, action: GetBoundScope.() -> Unit) {
        if (direction.isBlank()) {
            throw Exception()
        }
        if (companionViewModel.firstPresent) {
            companionViewModel.addDirection(direction)
            action(GetBoundScopeImpl(direction))
            companionViewModel.firstPresent = false
        }
    }

    companion object {

        @StyleRes
        const val nullStyleRes = 0

        const val NoGravity = -1
        const val Center = 0
        const val Bottom = 1
    }

    class CompanionViewModel : BaseViewModel() {

        private val directions = mutableSetOf<String>()

        override fun onRemove() {
            super.onRemove()
            directions.forEach { PostOffice.clearBounds(it) }
        }

        fun addDirection(direction: String) {
            directions.add(direction)
        }
    }

}