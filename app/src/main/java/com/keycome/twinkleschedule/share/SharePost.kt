package com.keycome.twinkleschedule.share

interface SharePost<K, T> : ShareOnly<K, T> {
    fun set(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>, variable: T): T
}