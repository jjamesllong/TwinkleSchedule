package com.keycome.twinkleschedule.share

import android.util.Log

class ShareSpace<K> {

    private var _isInitialized: Boolean = false

    fun isInitialized() = _isInitialized

    private var _shareTarget: MutableMap<K, Pair<Target<*>?, Int>>? = null

    val shareTarget
        get() = if (_isInitialized) _shareTarget!! else {
            _shareTarget = mutableMapOf()
            _isInitialized = true
            _shareTarget!!
        }

    fun releaseReference(key: K): Int? {
        return shareTarget[key]?.let {
            val afterReference = it.second - 1
            if (afterReference == 0) shareTarget.remove(key)
            else it.copy(second = afterReference).apply { shareTarget[key] = this }
            afterReference
        }.also {
            Log.d("ShareSpace", "remainReference of $key: $it")
        }
    }

    fun releaseAllReference(key: K): Int? {
        return shareTarget[key]?.let {
            val currentReference = it.second
            shareTarget.remove(key)
            currentReference
        }
    }

    operator fun <T> get(key: K): T? {
        return shareTarget[key]?.let { pair ->
            pair.first?.let { targetWrapper ->
                @Suppress(names = ["UNCHECKED_CAST"])
                targetWrapper.target as T
            }
        }
    }

    operator fun <T> set(key: K, variable: T): T {
        val t = object : Target<T> {
            override val target: T
                get() = variable
        }
        val p = shareTarget[key]?.copy(first = t) ?: Pair(first = t, second = 1)
        shareTarget[key] = p
        return variable
    }

    fun shareOnlyInit(key: K) {
        val p = shareTarget[key]?.let {
            it.copy(second = it.second + 1)
        } ?: Pair(null, 1)
        shareTarget[key] = p
    }

    fun sharePostInit(key: K) {
        shareOnlyInit(key)
    }

}

@Suppress(names = ["FunctionName"])
fun <K, T> ShareOnlyVariable(shareSpace: ShareSpace<K>, key: K): ShareOnlyDelegate<K, T> {
    return ShareOnlyDelegate(shareSpace, key)
}

@Suppress(names = ["FunctionName"])
fun <K, T> SharePostVariable(
    shareSpace: ShareSpace<K>,
    key: K,
    generator: () -> T
): SharePostDelegate<K, T> = SharePostDelegate(shareSpace, key, generator)