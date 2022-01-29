package com.keycome.twinkleschedule.activity

import android.util.Log
import androidx.activity.viewModels
import com.keycome.twinkleschedule.R.id.secondFragmentContainer
import com.keycome.twinkleschedule.base.BBaseActivity
import com.keycome.twinkleschedule.design.SecondDesign
import com.keycome.twinkleschedule.model.SecondViewModel
import com.keycome.twinkleschedule.pipette.SecondPipette
import kotlinx.coroutines.isActive
import kotlinx.coroutines.selects.select

class SecondActivity : BBaseActivity() {

    private val pipette: SecondPipette by pipettes()

    private val viewModel: SecondViewModel by viewModels()

    override suspend fun main() {
        supportDesign(SecondDesign(context = this))
        supportNavigation(secondFragmentContainer)
        defer {
            Log.d("SecondActivity", "defer")
        }

        viewModel.supportKey(SecondPipette::class.simpleName!!)

        while (coroutineScope.isActive) {
            select<Unit> {
                pipette.actionChannel.onReceive {
                    when (it) {
                        "button" -> pipette.requestChannel.trySend("randomInt")
                        else -> Unit
                    }
                }
            }
        }
    }
}