package com.keycome.twinkleschedule.base

import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.keycome.twinkleschedule.util.TextColor

abstract class BaseFragment : Fragment() {

    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }

    inline fun <reified VB : ViewBinding> VB?.acquire(): VB {
        return this ?: throw IllegalStateException(
            "request an instance of ${VB::class.qualifiedName} " +
                    "outside the lifecycle of onCreateView() and onDestroyView()"
        )
    }

}