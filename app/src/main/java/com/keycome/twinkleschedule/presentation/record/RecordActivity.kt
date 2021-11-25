package com.keycome.twinkleschedule.presentation.record

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.BaseActivity
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.ActivityRecordBinding

class RecordActivity : BaseActivity() {

    private lateinit var binding: ActivityRecordBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<RecordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mgr = supportFragmentManager
        val navHostFragment =
            mgr.findFragmentById(R.id.record_activity_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) super.onBackPressed()
    }

    companion object {
        const val NAV_KEY_TO_MANAGE_COURSE = "NAV_KEY_TO_MANAGE_COURSE"
        const val NAV_VALUE_TO_MANAGE_COURSE = true
    }

}