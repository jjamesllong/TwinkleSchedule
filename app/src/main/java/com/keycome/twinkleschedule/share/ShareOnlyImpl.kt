package com.keycome.twinkleschedule.share

import android.util.Log

class ShareOnlyImpl<K, V>(
    private val key: K,
    private val map: MutableMap<K, Pair<Target<*>?, Int>>
) : ShareOnly<K, V> {

    init {
        with(map[key]?.let { it.copy(second = it.second + 1) } ?: Pair(null, 1)) {
            map[key] = this
        }
        Log.d("ShareSpace", "referent: $key")
    }

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun get(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>): V? {
        return map[key]?.let { it.first?.target as V }
    }

}