package com.keycome.twinkleschedule

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.base.BaseActivity
import com.keycome.twinkleschedule.databinding.ActivityMainBinding
import com.keycome.twinkleschedule.util.TextColor

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setStatusBarAppearance(binding.root, TextColor.Dark)
    }
}