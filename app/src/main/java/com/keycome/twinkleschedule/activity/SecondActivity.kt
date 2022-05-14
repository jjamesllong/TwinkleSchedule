package com.keycome.twinkleschedule.activity

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseActivity
import com.keycome.twinkleschedule.databinding.ActivitySecondBinding
import com.keycome.twinkleschedule.model.SecondViewModel
import com.keycome.twinkleschedule.util.TextColor

class SecondActivity : BaseActivity() {

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
        setStatusBarAppearance(binding.root, TextColor.Dark)
        setContentView(binding.root)
        binding.secondNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.displayCoursesFragment -> {
                    if (navController.currentDestination?.id == R.id.displayCoursesFragment)
                        return@setOnItemSelectedListener true
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
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            R.id.displayCoursesFragment.let {
                if (navDestination.id == it && binding.secondNavigationBar.selectedItemId != it)
                    binding.secondNavigationBar.selectedItemId = it
            }
            navDestination.id.let {
                when (it) {
                    R.id.displayCoursesFragment, R.id.settingsFragment -> {
                        binding.secondNavigationBar.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.secondNavigationBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}