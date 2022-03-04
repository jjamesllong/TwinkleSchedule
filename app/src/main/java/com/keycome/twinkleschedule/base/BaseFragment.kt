package com.keycome.twinkleschedule.base

import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.keycome.twinkleschedule.utils.TextColor

abstract class BaseFragment : Fragment() {
    val attachToParentFalseToo = false

    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }
}