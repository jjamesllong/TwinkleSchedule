package com.keycome.twinkleschedule.delivery

import com.google.gson.Gson
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

object Pipette {

    val pipetteForLong = pipette<Long>()

    val pipetteForString = pipette<String>()

    val pipetteForInt = pipette<Int>()

    val gson = Gson()

    private fun <T> pipette() = MutableSharedFlow<Drop<T>>(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    suspend inline fun <T> MutableSharedFlow<Drop<T>>.subscribe(
        title: String,
        crossinline action: suspend (T) -> Unit
    ) {
        filter { it.first == title }.collect { action(it.second) }
    }
}