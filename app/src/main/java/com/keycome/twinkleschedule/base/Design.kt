package com.keycome.twinkleschedule.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class Design : DefaultLifecycleObserver {

    abstract fun onInit()

    abstract suspend fun onAsync()

    val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    protected val attachToParentFalse = false

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        coroutineScope.cancel()
        owner.lifecycle.removeObserver(this)
    }

    fun performInit(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
        onInit()
        coroutineScope.launch {
            onAsync()
        }
    }

}