package com.keycome.twinkleschedule.delivery

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

object Pipette {

    val pipetteForLong = MutableSharedFlow<Drop<Long>>(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    val pipetteForString = MutableSharedFlow<Drop<String>>(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
}