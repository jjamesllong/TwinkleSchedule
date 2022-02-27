package com.keycome.twinkleschedule.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseActivity2
import com.keycome.twinkleschedule.databinding.ActivitySecondBinding
import com.keycome.twinkleschedule.model.SecondViewModel

class SecondActivity : BaseActivity2() {

    private val viewModel: SecondViewModel by viewModels()

    private val binding: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.secondFragmentContainer
        ) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.secondNavigationBar.setOnItemSelectedListener {
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
}