package com.keycome.twinkleschedule.record.interval

enum class Day {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;

    fun toNumber() = when (this) {
        Monday -> 1
        Tuesday -> 2
        Wednesday -> 3
        Thursday -> 4
        Friday -> 5
        Saturday -> 6
        Sunday -> 7
    }

    fun toIndex() = when (this) {
        Monday -> Monday.ordinal
        Tuesday -> Tuesday.ordinal
        Wednesday -> Wednesday.ordinal
        Thursday -> Thursday.ordinal
        Friday -> Friday.ordinal
        Saturday -> Saturday.ordinal
        Sunday -> Sunday.ordinal
    }

    companion object {

        fun toNumber(day: Day) = day.toNumber()

        fun fromNumber(number: Int): Day = fromIndex(number - 1)

        fun toIndex(day: Day) = day.toIndex()

        fun fromIndex(index: Int): Day = when (index) {
            0 -> Monday
            1 -> Tuesday
            2 -> Wednesday
            3 -> Thursday
            4 -> Friday
            5 -> Saturday
            6 -> Sunday
            else -> throw Exception()
        }
    }
}