package com.keycome.twinkleschedule.record.interval

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Time(val hour: Int, val minute: Int, val second: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    init {
        if (hour !in 0..23) throw IllegalArgumentException("hour should between [0..23]")
        if (minute !in 0..59) throw IllegalArgumentException("minute should between [0..59]")
        if (second !in 0..59) throw IllegalArgumentException("second should between [0..59]")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(hour)
        parcel.writeInt(minute)
        parcel.writeInt(second)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        val builder = StringBuilder().apply {
            val separator = ":"
            align(hour)
            append(separator)
            align(minute)
            append(separator)
            align(second)
        }
        return builder.toString()
    }


    fun formatWithoutSecond24(): String {
        val builder = StringBuilder().apply {
            append(if (hour < 10) "0$hour" else hour.toString())
            append(":")
            append(if (minute < 10) "0$minute" else minute.toString())
        }
        return builder.toString()
    }

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

    fun toSeconds(): Int {
        return hour * 60 * 60 + minute * 60 + second
    }

    operator fun plus(seconds: Int): Time {
        val totalSeconds = toSeconds() + seconds
        return if (totalSeconds >= DAILY_SECONDS) {
            from((totalSeconds % DAILY_SECONDS))
        } else {
            from(totalSeconds)
        }
    }

    operator fun minus(time: Time): Int {
        val end = toSeconds()
        val start = time.toSeconds()
        return if (end >= start) end - start else end - start + DAILY_SECONDS
    }

    companion object {

        const val DAILY_SECONDS = 24 * 60 * 60
        const val HOUR_SECONDS = 60 * 60
        const val MINUTE_SECONDS = 60


        private fun StringBuilder.align(number: Int) {
            if (number < 10) {
                append(0)
            }
            append(number)
        }

        @Suppress("unused")
        @JvmField
        val CREATOR = object : Parcelable.Creator<Time> {
            override fun createFromParcel(parcel: Parcel): Time {
                return Time(parcel)
            }

            override fun newArray(size: Int): Array<Time?> {
                return arrayOfNulls(size)
            }
        }

        fun from(hour: Int, minute: Int, seconds: Int): Time {
            return Time(hour, minute, seconds)
        }

        fun from(string: String): Time {
            val s = string.split(delimiters = arrayOf(":"), ignoreCase = false, limit = 0)
            return when (s.size) {
                2 -> Time(hour = s[0].toInt(), minute = s[1].toInt(), second = 0)
                3 -> Time(hour = s[0].toInt(), minute = s[1].toInt(), second = s[2].toInt())
                else -> throw Exception()
            }
        }

        fun from(seconds: Int): Time {
            if (seconds < 0) {
                throw IllegalArgumentException("seconds should not be negative")
            }
            val h = seconds / HOUR_SECONDS
            val m = seconds % HOUR_SECONDS / MINUTE_SECONDS
            val s = seconds % HOUR_SECONDS % MINUTE_SECONDS
            return Time(h, m, s)
        }

        fun now(): Time {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            return Time(hour, minute, second)
        }
    }
}
