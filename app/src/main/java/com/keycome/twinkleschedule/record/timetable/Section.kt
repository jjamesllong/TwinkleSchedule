package com.keycome.twinkleschedule.record.timetable

import android.os.Parcel
import android.os.Parcelable
import com.keycome.twinkleschedule.record.interval.Time

data class Section(val from: Time, val to: Time) : Parcelable {

    val duration: Int
        get() = to - from

    constructor(parcel: Parcel) : this(
        parcel.readParcelable<Time>(Time::class.java.classLoader) ?: throw Exception(),
        parcel.readParcelable<Time>(Time::class.java.classLoader) ?: throw Exception()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(from, flags)
        parcel.writeParcelable(to, flags)
    }

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<Section> {
            override fun createFromParcel(parcel: Parcel): Section {
                return Section(parcel)
            }

            override fun newArray(size: Int): Array<Section?> {
                return arrayOfNulls(size)
            }
        }
    }
}