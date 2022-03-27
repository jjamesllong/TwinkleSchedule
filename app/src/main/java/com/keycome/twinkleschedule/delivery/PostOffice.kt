package com.keycome.twinkleschedule.delivery

import com.keycome.twinkleschedule.extension.removeWhen

object PostOffice {

    private val setForLong = mutableSetOf<Bound<Long>>()

    private val setForInt = mutableSetOf<Bound<Int>>()

    private val setForString = mutableSetOf<Bound<String>>()

    fun clearBounds(direction: String) {
        setForLong.removeWhen { it.direction == direction }
        setForInt.removeWhen { it.direction == direction }
    }

    fun putLongBound(bound: Bound<Long>) {
        setForLong.add(bound)
    }

    fun getLongBound(direction: String, title: String): Bound<Long>? {
        return setForLong.find { it.direction == direction && it.title == title }
    }

    fun putIntBound(bound: Bound<Int>) {
        setForInt.add(bound)
    }

    fun getIntBound(direction: String, title: String): Bound<Int>? {
        return setForInt.find { it.direction == direction && it.title == title }
    }

    fun putStringBound(bound: Bound<String>) {
        setForString.add(bound)
    }

    fun getStringBound(direction: String, title: String): Bound<String>? {
        return setForString.find { it.direction == direction && it.title == title }
    }
}