package com.keycome.twinkleschedule.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.keycome.twinkleschedule.util.TextColor

abstract class BaseActivity : AppCompatActivity() {
    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }
}