package com.keycome.twinkleschedule.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseActivity
import com.keycome.twinkleschedule.databinding.ActivitySecondBinding
import com.keycome.twinkleschedule.util.TextColor
import com.keycome.twinkleschedule.viewmodel.SecondViewModel

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
        setContentView(binding.root)
        setStatusBarAppearance(binding.root, TextColor.Dark)
    }
}