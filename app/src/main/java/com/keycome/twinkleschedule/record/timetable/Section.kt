package com.keycome.twinkleschedule.record.timetable

import android.os.Parcel
import android.os.Parcelable
import com.keycome.twinkleschedule.record.interval.Time

data class Section(val order: Int, val from: Time, val to: Time) : Parcelable {

    val duration: Int
        get() = to - from

    constructor(parcel: Parcel) : this(
        order = parcel.readInt(),
        from = Time.from(parcel.readInt(), parcel.readInt(), parcel.readInt()),
        to = Time.from(parcel.readInt(), parcel.readInt(), parcel.readInt())
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(order)
        parcel.writeInt(from.hour)
        parcel.writeInt(from.minute)
        parcel.writeInt(from.second)
        parcel.writeInt(to.hour)
        parcel.writeInt(to.minute)
        parcel.writeInt(to.second)
    }

    override fun toString(): String {
        return StringBuilder().apply {
            align(from.hour)
            append(":")
            align(from.minute)
            append("~")
            align(to.hour)
            append(":")
            align(to.minute)
            append("@")
            align(order)
        }.toString()
    }

    companion object {

        @Suppress("unused")
        @JvmField
        val CREATOR = object : Parcelable.Creator<Section> {
            override fun createFromParcel(parcel: Parcel): Section {
                return Section(parcel)
            }

            override fun newArray(size: Int): Array<Section?> {
                return arrayOfNulls(size)
            }
        }

        fun fromString(string: String): Section {
            val s = string.split(delimiters = arrayOf("~", "@"), ignoreCase = false, limit = 0)
            return Section(s[2].toInt(), Time.from(s[0]), Time.from(s[1]))
        }

        private fun StringBuilder.align(number: Int) {
            if (number < 10) {
                append(0)
            }
            append(number)
        }
    }
}