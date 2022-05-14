package com.keycome.twinkleschedule.record.interval

import android.os.Parcel
import android.os.Parcelable

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
            (totalSeconds % DAILY_SECONDS).fromSeconds()
        } else totalSeconds.fromSeconds()
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

        @JvmField
        val CREATOR = object : Parcelable.Creator<Time> {
            override fun createFromParcel(parcel: Parcel): Time {
                return Time(parcel)
            }

            override fun newArray(size: Int): Array<Time?> {
                return arrayOfNulls(size)
            }
        }

        fun Int.fromSeconds(): Time {
            val seconds = this
            if (seconds < 0) {
                throw IllegalArgumentException("seconds should not be negative")
            }
            val h = seconds / HOUR_SECONDS
            val m = seconds % HOUR_SECONDS / MINUTE_SECONDS
            val s = seconds % HOUR_SECONDS % MINUTE_SECONDS
            return Time(h, m, s)
        }
    }
}
