package com.keycome.twinkleschedule

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.keycome.twinkleschedule.activity.SecondActivity
import com.keycome.twinkleschedule.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        startActivity(Intent(this, SecondActivity::class.java))
        finish()
    }
}