package com.keycome.twinkleschedule.delivery

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

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

    val pipetteForInt = MutableSharedFlow<Drop<Int>>()

    suspend inline fun <T> MutableSharedFlow<Drop<T>>.subscribe(
        title: String,
        crossinline action: suspend (T) -> Unit
    ) {
        filter { it.first == title }.collect { action(it.second) }
    }
}