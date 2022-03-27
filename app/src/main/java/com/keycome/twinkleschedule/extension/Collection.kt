package com.keycome.twinkleschedule.extension

inline fun <E> MutableCollection<E>.removeWhen(predicate: (E) -> Boolean) {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val element = iterator.next()
        val bool = predicate(element)
        if (bool) {
            iterator.remove()
        }
    }
}