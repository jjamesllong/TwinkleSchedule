package com.keycome.twinkleschedule.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class LifecycleViewModel : ViewModel() {

    abstract suspend fun onPlace()

    abstract fun onRemove()

    init {
        viewModelScope.launch { onPlace() }
    }

    final override fun onCleared() {
        super.onCleared()
        onRemove()
    }
}