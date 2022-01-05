package com.keycome.twinkleschedule.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel :
    ViewModel(),
    CoroutineScope by CoroutineScope(Dispatchers.Unconfined) {

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()
}

