package com.keycome.twinkleschedule.model

data class Time(val hour: Int, val minute: Int, val second: Int) {
    fun to24StyleString(withoutSecond: Boolean = true): String {
        val builder = StringBuilder().apply {
            append(if (hour < 10) "0$hour" else hour)
            append(":")
            append(if (minute < 10) "0$minute" else minute)
            if (!withoutSecond) {
                append(":")
                append(if (second < 10) "0$second" else second)
            }
        }
        return builder.toString()
    }
}

data class Date(val year: Int, val month: Int, val dayOfMonth: Int) {
    init {
        if (year !in 1970..9999)
            throw IllegalArgumentException("the format of parameter year is incorrect")
        if (month !in 1..12)
            throw IllegalArgumentException("parameter month should between [1, 12]")
        if (dayOfMonth !in 1..31)
            throw IllegalArgumentException("parameter dayOfYear should between [1, 31]")
    }

    fun toHyphenDateString(): String {
        val builder = StringBuilder().apply {
            append(year)
            append("-")
            append(if (month < 10) "0$month" else month)
            append("-")
            append(if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth)
        }
        return builder.toString()
    }

    fun toDotDateString(): String {
        val builder = StringBuffer().apply {
            append(year)
            append(". ")
            append(month)
            append(". ")
            append(dayOfMonth)
        }
        return builder.toString()
    }
}

enum class Day { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }