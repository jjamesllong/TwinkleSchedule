package com.keycome.twinkleschedule.model.horizon

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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

    fun toMillis(): Long {
        val format = SimpleDateFormat(HYPHEN_DATE_STRING, Locale.CHINA)
        val jdkDate = format.parse(toHyphenDateString())
        return jdkDate?.time ?: throw ParseException("failed to parse date $this", 0)
    }

    companion object {

        private const val HYPHEN_DATE_STRING = "yyyy-MM-dd"

    }
}
