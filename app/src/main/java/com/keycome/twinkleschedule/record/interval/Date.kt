package com.keycome.twinkleschedule.record.interval

import android.os.Parcel
import android.os.Parcelable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class Date(val year: Int, val month: Int, val dayOfMonth: Int) : Parcelable {

    init {
        if (year !in 1970..9999)
            throw IllegalArgumentException("the format of parameter year is incorrect")
        if (month !in 1..12)
            throw IllegalArgumentException("parameter month should between [1, 12]")
        if (dayOfMonth !in 1..31)
            throw IllegalArgumentException("parameter dayOfYear should between [1, 31]")
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(dayOfMonth)
    }

    override fun describeContents(): Int {
        return 0
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

    fun toMilliSeconds(): Long {
        val format = SimpleDateFormat(HYPHEN_DATE_STRING, Locale.getDefault())
        val jdkDate = format.parse(toHyphenDateString())
        return jdkDate?.time ?: throw ParseException("failed to parse date $this", 0)
    }

    companion object {

        private const val HYPHEN_DATE_STRING = "yyyy-MM-dd"

        @JvmField
        val CREATOR = object : Parcelable.Creator<Date> {
            override fun createFromParcel(source: Parcel): Date {
                return Date(source)
            }

            override fun newArray(size: Int): Array<Date?> {
                return arrayOfNulls(size)
            }
        }

        fun fromString(dateString: String): Date {
            val y = dateString.substring(0, 4).toInt()
            val m = dateString.substring(5, 7).toInt()
            val d = dateString.substring(8, 10).toInt()
            return Date(y, m, d)
        }

        fun currentDate(): Date {
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val dayOfMonth = calender.get(Calendar.DAY_OF_MONTH)
            return Date(year, month, dayOfMonth)
        }
    }
}
