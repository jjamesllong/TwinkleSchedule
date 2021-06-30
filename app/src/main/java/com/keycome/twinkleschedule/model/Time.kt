package com.keycome.twinkleschedule.model

data class Time(val hour: Int, val minute: Int, val second: Int)

data class Date(val year: Int, val month: Int, val dayOfMonth: Int) {
    init {
        if (year !in 1970..9999)
            throw IllegalArgumentException("the format of parameter year is incorrect")
        if (month !in 1..12)
            throw IllegalArgumentException("parameter month should between [1, 12]")
        if (dayOfMonth !in 1..31)
            throw IllegalArgumentException("parameter dayOfYear should between [1, 31]")
    }
}

enum class Day { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }