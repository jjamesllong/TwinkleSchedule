package com.keycome.twinkleschedule.model

import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.pipette.SecondPipette
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlin.random.Random

class SecondViewModel : BaseViewModel() {

    private val pipette: SecondPipette by pipettes()

    init {
        launch {
            while (isActive) {
                select<Unit> {
                    pipette.requestChannel.onReceive {
                        when (it) {
                            "randomInt" -> pipette.modelChannel.trySend(Random.nextInt(until = 100))
                        }
                    }
                }
            }
        }
    }
}