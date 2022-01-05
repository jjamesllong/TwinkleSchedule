package com.keycome.twinkleschedule.share

interface Releaser<K> {

    fun releaseReference(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>): Int?

    fun releaseAllReference(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>): Int?
}