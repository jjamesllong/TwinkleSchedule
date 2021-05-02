package com.keycome.twinkleschedule.presentation.display

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.BaseActivity
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.ActivityDisplayBinding

class DisplayActivity : BaseActivity() {
    private lateinit var binding: ActivityDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment
        val navController = navHostFragment.navController
    }
}