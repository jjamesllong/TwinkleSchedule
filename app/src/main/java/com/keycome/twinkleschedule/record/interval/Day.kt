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

    fun toOrdinal() = when (this) {
        Monday -> Monday.ordinal
        Tuesday -> Tuesday.ordinal
        Wednesday -> Wednesday.ordinal
        Thursday -> Thursday.ordinal
        Friday -> Friday.ordinal
        Saturday -> Saturday.ordinal
        Sunday -> Sunday.ordinal
    }

    companion object {

        fun fromNumber(number: Int): Day = fromOrdinal(number - 1)

        fun fromOrdinal(ordinal: Int): Day = when (ordinal) {
            0 -> Monday
            1 -> Tuesday
            2 -> Wednesday
            3 -> Thursday
            4 -> Friday
            5 -> Saturday
            6 -> Sunday
            else -> throw Exception()
        }

        fun fromString(s: String): Day = when (s.trim().uppercase()) {
            "MONDAY" -> Monday
            "TUESDAY" -> Tuesday
            "WEDNESDAY" -> Wednesday
            "THURSDAY" -> Thursday
            "FRIDAY" -> Friday
            "SATURDAY" -> Saturday
            "SUNDAY" -> Sunday
            else -> throw Exception()
        }

    }
}