package com.keycome.twinkleschedule.design

import android.content.Context
import android.view.View
import com.keycome.twinkleschedule.base.Design
import com.keycome.twinkleschedule.databinding.ActivitySecondBinding
import com.keycome.twinkleschedule.extension.layoutInflater
import com.keycome.twinkleschedule.extension.root
import com.keycome.twinkleschedule.extension.viewBindings
import com.keycome.twinkleschedule.pipette.SecondPipette
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

class SecondDesign(val context: Context) : Design() {

    private val pipette: SecondPipette by pipettes()

    override val rootView: View by viewBindings(coroutineScope, {
        ActivitySecondBinding.inflate(
            context.layoutInflater,
            context.root,
            false
        )
    }) {

        coroutineScope.launch {
            while (isActive) {
                select<Unit> {
                    pipette.modelChannel.onReceive {
                        secondText.text = it.toString()
                    }
                }
            }
        }

        secondButton.setOnClickListener {
            pipette.actionChannel.trySend("button")
        }
    }
}