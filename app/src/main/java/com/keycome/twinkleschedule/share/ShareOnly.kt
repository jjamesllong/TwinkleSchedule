package com.keycome.twinkleschedule.share

interface ShareOnly<K, T> {
    fun get(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>): T?
}