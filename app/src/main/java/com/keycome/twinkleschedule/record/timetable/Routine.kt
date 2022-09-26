package com.keycome.twinkleschedule.record.timetable

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine")
data class Routine(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "routine_id")
    val routineId: Long,

    @ColumnInfo(name = "master_id")
    val masterId: Long,

    @ColumnInfo(name = "routine_name")
    val routineName: String,

    @ColumnInfo(name = "start_date")
    val startDate: String,

    @ColumnInfo(name = "section_list")
    val sectionList: List<String>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        routineId = parcel.readLong(),
        masterId = parcel.readLong(),
        routineName = parcel.readString() ?: "",
        startDate = parcel.readString() ?: "",
        sectionList = arrayOfNulls<String>(parcel.readInt()).also {
            for (i in it.indices) {
                it[i] = parcel.readString()
            }
        }.map { it ?: "" }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(routineId)
        parcel.writeLong(masterId)
        parcel.writeString(routineName)
        parcel.writeString(startDate)
        parcel.writeInt(sectionList.size)
        sectionList.forEach { parcel.writeString(it) }
    }

    override fun describeContents(): Int = 0

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<Routine> {
            override fun createFromParcel(parcel: Parcel): Routine {
                return Routine(parcel)
            }

            override fun newArray(size: Int): Array<Routine?> {
                return arrayOfNulls(size)
            }
        }
    }

}