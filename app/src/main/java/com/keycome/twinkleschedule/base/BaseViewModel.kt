package com.keycome.twinkleschedule.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private var firstPresent: Boolean = true

    final override fun onCleared() {
        super.onCleared()
        onRemove()
    }

    open suspend fun onPlace() {}

    open fun onRemove() {}

    fun onFirstPresent(action: () -> Unit) {
        if (firstPresent) {
            firstPresent = false
            action()
        }
    }
}

