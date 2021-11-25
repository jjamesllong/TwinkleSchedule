package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.BaseActivity
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.ActivityConfigurationBinding

class ConfigurationActivity : BaseActivity() {
    private lateinit var binding: ActivityConfigurationBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: ConfigurationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ConfigurationViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.configuration_activity_host_fragment) as
                    NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) super.onBackPressed()
    }

    companion object {
        const val NAV_KEY_TO_MANAGE_SCHEDULE = "NAV_KEY_TO_MANAGE_SCHEDULE"
        const val NAV_VALUE_TO_MANAGE_SCHEDULE = "TO_MANAGE_SCHEDULE"
    }
}