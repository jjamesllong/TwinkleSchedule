package com.keycome.twinkleschedule.delivery

import kotlin.reflect.KProperty

class ShareOnlyDelegate<K, T>(private val shareSpace: ShareSpace<K>, private val key: K) {

    init {
        shareSpace.shareOnlyInit(key)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return shareSpace[key]
    }
}