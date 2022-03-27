package com.keycome.twinkleschedule.base

import com.keycome.twinkleschedule.delivery.*

abstract class BaseViewModel : LifecycleViewModel() {

    var firstPresent: Boolean = true

    override suspend fun onPlace() {}

    override fun onRemove() {}

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

