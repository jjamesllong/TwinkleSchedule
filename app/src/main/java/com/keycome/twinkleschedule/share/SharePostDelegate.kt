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
        return shareSpace[key] ?: shareSpace.set<T>(
            key,
            _generator?.invoke().also { _generator = null } ?: throw Exception()
        )
    }
}