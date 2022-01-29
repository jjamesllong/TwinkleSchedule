package com.keycome.twinkleschedule.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    private var _key: String? = null
    fun supportKey(key: String) {
        _key = key
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
        try {
            Pipette.releasePipettes(_key ?: throw Exception())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()
}

