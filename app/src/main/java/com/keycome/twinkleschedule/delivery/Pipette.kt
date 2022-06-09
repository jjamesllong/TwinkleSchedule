package com.keycome.twinkleschedule.delivery

import com.google.gson.Gson
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

object Pipette {

    val forEvent = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    val forLong = pipette<Long>()

    val forString = pipette<String>()

    val forInt = pipette<Int>()

    val gson = Gson()

    private fun <T> pipette() = MutableSharedFlow<Drop<T>>(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    suspend inline fun MutableSharedFlow<String>.distribute(action: () -> String) {
        emit(action())
    }

    suspend inline fun MutableSharedFlow<String>.subscribe(
        title: String,
        crossinline action: suspend () -> Unit
    ) {
        filter { it == title }.collect { action() }
    }

    suspend inline fun <T> MutableSharedFlow<Drop<T>>.distribute(
        title: String,
        action: () -> T
    ) {
        emit(Drop(title, action()))
    }

    suspend inline fun <T> MutableSharedFlow<Drop<T>>.subscribe(
        title: String,
        crossinline action: suspend (T) -> Unit
    ) {
        filter { it.first == title }.collect { action(it.second) }
    }
}