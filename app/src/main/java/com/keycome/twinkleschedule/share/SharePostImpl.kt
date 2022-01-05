package com.keycome.twinkleschedule.share

class SharePostImpl<K, V>(
    private val key: K,
    private val map: MutableMap<K, Pair<Target<*>?, Int>>,
) : ShareOnly<K, V> by ShareOnlyImpl(key, map), SharePost<K, V> {

    override fun set(key: K, map: MutableMap<K, Pair<Target<*>?, Int>>, variable: V): V {
        val newTarget: Target<V> = object : Target<V> {
            override val target: V
                get() = variable
        }
        map[key]?.let {
            map[key] = it.copy(first = newTarget)
        } ?: Pair(newTarget, 1).let { map[key] = it }
        return variable
    }

}