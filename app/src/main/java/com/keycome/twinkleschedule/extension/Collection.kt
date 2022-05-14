package com.keycome.twinkleschedule.extension

inline fun <E> MutableCollection<E>.removeWhen(predicate: (E) -> Boolean) {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val element = iterator.next()
        if (predicate(element)) {
            iterator.remove()
        }
    }
}