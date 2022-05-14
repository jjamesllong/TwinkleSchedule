package com.keycome.twinkleschedule.base

import androidx.lifecycle.ViewModel

abstract class LifecycleViewModel : ViewModel() {

    abstract suspend fun onPlace()

    abstract fun onRemove()

    final override fun onCleared() {
        super.onCleared()
        onRemove()
    }
}