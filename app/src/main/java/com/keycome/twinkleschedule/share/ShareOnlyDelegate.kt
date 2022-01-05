package com.keycome.twinkleschedule.share

import kotlin.reflect.KProperty

class ShareOnlyDelegate<K, V>(
    private val key: K,
    private val map: MutableMap<K, Pair<Target<*>?, Int>>
) : ShareOnly<K, V> by ShareOnlyImpl(key, map) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V? {
        return get(key, map)
    }

}