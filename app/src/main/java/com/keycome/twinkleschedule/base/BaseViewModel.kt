package com.keycome.twinkleschedule.base

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.share.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {

    abstract fun onInit()

    abstract suspend fun onAsync()

    val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    private var _key: String? = null
    fun supportKey(key: String) {
        _key = key
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        coroutineScope.cancel()
        owner.lifecycle.removeObserver(this)
    }

    override fun onCleared() {
        Log.d("BaseViewModel", "${this::class.simpleName} onCleared()")
        super.onCleared()
//        _key?.let { key ->
//            Pipette.releasePipettes(key)
//        }
    }

    fun performInit(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
        onInit()
        coroutineScope.launch {
            onAsync()
        }
    }
}

abstract class BaseViewModel2 : ViewModel() {
    companion object {
        val shareSpace = ShareSpace<String>()

        fun <T> sharePostVariable(key: String, generator: () -> T): SharePostDelegate<String, T> {
            return SharePostVariable(shareSpace, key, generator)
        }

        fun <T> shareOnlyVariable(key: String): ShareOnlyDelegate<String, T> {
            return ShareOnlyVariable(shareSpace, key)
        }

        fun release(vararg key: String) {
            for (k in key)
                shareSpace.releaseReference(k)
        }
    }
}

