package com.keycome.twinkleschedule.data.preference

class PreferenceWrapper<T : Any>(val owner: String) {

    private var target: T? = null

    private val subscribers: MutableSet<(T) -> Unit> = mutableSetOf()

    fun add(subscriber: (T) -> Unit): Boolean {
        return if (subscribers.find { it === subscriber } == null) {
            subscribers.add(subscriber)
            true
        } else false
    }

    fun remove(subscriber: (T) -> Unit) {
        subscribers.remove(subscriber)
    }

    fun setValue(value: T) {
        this.target = value
        subscribers.forEach { action -> action(value) }
    }

    fun getValue(): T? {
        return this.target
    }
}