package com.keycome.twinkleschedule.record.span

import android.os.Parcel
import android.os.Parcelable

data class Time(val hour: Int, val minute: Int, val second: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(hour)
        parcel.writeInt(minute)
        parcel.writeInt(second)
    }

    override fun describeContents(): Int {
        return 0
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
        return hour * 3600 + minute * 60 + second
    }

    companion object CREATOR : Parcelable.Creator<Time> {
        override fun createFromParcel(parcel: Parcel): Time {
            return Time(parcel)
        }

        override fun newArray(size: Int): Array<Time?> {
            return arrayOfNulls(size)
        }
    }
}
