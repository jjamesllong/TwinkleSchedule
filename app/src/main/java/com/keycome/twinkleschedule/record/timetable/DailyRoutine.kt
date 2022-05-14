package com.keycome.twinkleschedule.record.timetable

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keycome.twinkleschedule.record.interval.Date

@Entity(tableName = "daily_routine")
data class DailyRoutine(
    @PrimaryKey
    @ColumnInfo(name = "daily_routine_id")
    val dailyRoutineId: Long,

    @ColumnInfo(name = "parent_schedule_id")
    val parentScheduleId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "start_date")
    val startDate: Date,

    @ColumnInfo(name = "routines")
    val routines: List<Section>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dailyRoutineId = parcel.readLong(),
        parentScheduleId = parcel.readLong(),
        name = parcel.readString() ?: throw Exception(),
        startDate = parcel.readParcelable<Date>(Date::class.java.classLoader) ?: throw Exception(),
        routines = parcel.createTypedArrayList(Section.CREATOR)?.toList() ?: throw Exception()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dailyRoutineId)
        parcel.writeLong(parentScheduleId)
        parcel.writeString(name)
        parcel.writeParcelable(startDate, flags)
        parcel.writeTypedList(routines)
    }

    override fun describeContents(): Int = 0

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<DailyRoutine> {
            override fun createFromParcel(parcel: Parcel): DailyRoutine {
                return DailyRoutine(parcel)
            }

            override fun newArray(size: Int): Array<DailyRoutine?> {
                return arrayOfNulls(size)
            }
        }
    }

}