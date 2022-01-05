package com.keycome.twinkleschedule.share

import kotlin.reflect.KProperty

class SharePostDelegate<K, V>(
    private val key: K,
    private val map: MutableMap<K, Pair<Target<*>?, Int>>,
    generator: () -> V
) : SharePost<K, V> by SharePostImpl(key, map) {

    private var _generator: (() -> V)? = generator

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return map[key]?.let { pair ->
            pair.first?.let { target ->
                @Suppress(names = ["UNCHECKED_CAST"])
                target.target as V
            }
        } ?: set(key, map, _generator!!()).also { _generator = null }
    }
}