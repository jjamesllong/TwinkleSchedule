package com.keycome.twinkleschedule.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.keycome.twinkleschedule.extension.activity.viewModelDelegate
import com.keycome.twinkleschedule.util.TextColor

abstract class BaseActivity : AppCompatActivity() {

    protected inline fun <reified VM : BaseViewModel> viewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ) = viewModelDelegate<VM>(factoryProducer)

    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }
}