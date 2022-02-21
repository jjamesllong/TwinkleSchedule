package com.keycome.twinkleschedule.design

import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.activity.SecondActivity
import com.keycome.twinkleschedule.base.BindingDesign
import com.keycome.twinkleschedule.databinding.ActivitySecondBinding
import com.keycome.twinkleschedule.pipette.SecondPipette

class SecondDesign(private val activity: SecondActivity) : BindingDesign<ActivitySecondBinding>() {

    private val pipette: SecondPipette by pipettes()

    override fun onBind(binding: ActivitySecondBinding) {

        binding.secondNavigationBar.setOnItemSelectedListener {
            val navController = activity.navController
            when (it.itemId) {
                R.id.displayCoursesFragment2 -> {
                    navController.navigateUp()
                    true
                }
                R.id.settingsFragment -> {
                    if (navController.currentDestination?.id == R.id.settingsFragment)
                        return@setOnItemSelectedListener true
                    else {
                        navController.navigate(R.id.settingsFragment)
                        true
                    }
                }
                else -> false
            }
        }

    }

    override fun onInit() {
    }

    override suspend fun onAsync() {
    }
}