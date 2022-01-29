package com.keycome.twinkleschedule.share

import kotlin.reflect.KProperty

class SharePostDelegate<K, T>(
    private val shareSpace: ShareSpace<K>,
    private val key: K,
    generator: () -> T
) {
    private var _generator: (() -> T)? = generator

    init {
        shareSpace.sharePostInit(key)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val result = shareSpace[key] ?: shareSpace.set(
            key,
            _generator?.invoke() ?: throw Exception()
        )
        if (_generator != null) _generator = null
        return result
    }
}