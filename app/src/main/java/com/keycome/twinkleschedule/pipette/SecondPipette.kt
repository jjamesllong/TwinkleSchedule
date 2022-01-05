package com.keycome.twinkleschedule.pipette

import com.keycome.twinkleschedule.base.Pipette
import kotlinx.coroutines.channels.Channel

class SecondPipette : Pipette() {
    val actionChannel = Channel<String>()
    val requestChannel = Channel<String>()
    val modelChannel = Channel<Int>()
}