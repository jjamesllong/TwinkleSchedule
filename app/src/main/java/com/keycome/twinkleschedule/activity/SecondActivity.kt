package com.keycome.twinkleschedule.activity

import android.util.Log
import androidx.activity.viewModels
import com.keycome.twinkleschedule.R.id.secondFragmentContainer
import com.keycome.twinkleschedule.base.BBaseActivity
import com.keycome.twinkleschedule.databinding.ActivitySecondBinding
import com.keycome.twinkleschedule.design.SecondDesign
import com.keycome.twinkleschedule.model.SecondViewModel
import com.keycome.twinkleschedule.pipette.SecondPipette
import kotlinx.coroutines.awaitCancellation

class SecondActivity : BBaseActivity() {

    private val pipette: SecondPipette by pipettes()

    private val viewModel: SecondViewModel by viewModels()

    override fun onInit() {
        Log.d("SecondActivity", "onInit()")
        supportNavigation(secondFragmentContainer)
        supportBindingDesign(SecondDesign(activity = this)) {
            ActivitySecondBinding.inflate(layoutInflater)
        }
        viewModel.supportKey(SecondPipette::class.simpleName!!)
    }

    override suspend fun onAsync() {
        defer {
            Log.d("SecondActivity", "defer")
        }

        awaitCancellation()
    }
}