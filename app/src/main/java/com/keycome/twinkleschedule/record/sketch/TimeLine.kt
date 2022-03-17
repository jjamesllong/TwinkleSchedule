package com.keycome.twinkleschedule.record.sketch

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.keycome.twinkleschedule.record.span.Date
import com.keycome.twinkleschedule.record.span.Time

data class TimeLine(
    val id: Long,
    val name: String,
    val startDate: Date,
    @SerializedName(value = "timeList", alternate = ["lineList"])
    val timeList: List<Time>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        name = parcel.readString() ?: throw Exception(),
        startDate = parcel.readParcelable(Date::class.java.classLoader) ?: throw Exception(),
        timeList = parcel.createTypedArrayList(Time.CREATOR)?.toList() ?: throw Exception()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeParcelable(startDate, flags)
        parcel.writeTypedList(timeList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimeLine> {
        override fun createFromParcel(parcel: Parcel): TimeLine {
            return TimeLine(parcel)
        }

        override fun newArray(size: Int): Array<TimeLine?> {
            return arrayOfNulls(size)
        }
    }
}