package com.keycome.twinkleschedule.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.keycome.twinkleschedule.extension.activities.viewModelDelegate
import com.keycome.twinkleschedule.util.TextColor

abstract class BaseActivity : AppCompatActivity() {

    protected inline fun <reified VM : BaseViewModel> viewModels(): Lazy<VM> {
        return viewModelDelegate()
    }

    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }
}