package com.keycome.twinkleschedule.base

import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.keycome.twinkleschedule.extension.fragments.activityViewModelDelegate
import com.keycome.twinkleschedule.extension.fragments.viewModelDelegate
import com.keycome.twinkleschedule.util.TextColor

abstract class BaseFragment : Fragment() {

    protected inline fun <reified VM : BaseViewModel> viewModels(): Lazy<VM> {
        return viewModelDelegate()
    }

    protected inline fun <reified VM : BaseViewModel> activityViewModels(): Lazy<VM> {
        return activityViewModelDelegate()
    }

    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }

}