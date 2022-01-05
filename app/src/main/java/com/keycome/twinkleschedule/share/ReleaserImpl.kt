package com.keycome.twinkleschedule.share

import android.util.Log

class ReleaserImpl<K> : Releaser<K> {
    override fun releaseReference(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>): Int? {
        return map[key]?.let {
            val afterReference = it.second - 1
            if (afterReference == 0) map.remove(key)
            else it.copy(second = afterReference).apply { map[key] = this }
            afterReference
        }.also {
            Log.d("ShareSpace", "remainReference of $key: $it")
        }
    }

    override fun releaseAllReference(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>): Int? {
        return map[key]?.let {
            val currentReference = it.second
            map.remove(key)
            currentReference
        }
    }
}