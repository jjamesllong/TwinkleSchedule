package com.keycome.twinkleschedule.record.timetable

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keycome.twinkleschedule.record.interval.Day

@Entity(tableName = "course")
data class Course(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    val courseId: Long,

    @ColumnInfo(name = "parent_schedule_id")
    val parentScheduleId: Long,

    val title: String,

    val day: Day,

    val section: List<Int>,

    val week: List<Int>,

    val teacher: String,

    val classroom: String,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        courseId = parcel.readLong(),
        parentScheduleId = parcel.readLong(),
        title = parcel.readString() ?: throw Exception(),
        day = Day.fromOrdinal(parcel.readInt()),
        section = IntArray(parcel.readInt()) { parcel.readInt() }.toList(),
        week = IntArray(parcel.readInt()) { parcel.readInt() }.toList(),
        teacher = parcel.readString() ?: throw Exception(),
        classroom = parcel.readString() ?: throw Exception()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(courseId)
        parcel.writeLong(parentScheduleId)
        parcel.writeString(title)
        parcel.writeInt(day.toOrdinal())
        parcel.writeInt(section.size)
        section.forEach { parcel.writeInt(it) }
        parcel.writeInt(week.size)
        week.forEach { parcel.writeInt(it) }
        parcel.writeString(teacher)
        parcel.writeString(classroom)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR {

        @JvmField
        val CREATOR = object : Parcelable.Creator<Course> {

            override fun createFromParcel(parcel: Parcel): Course = Course(parcel)

            override fun newArray(size: Int): Array<Course?> = arrayOfNulls(size)

        }
    }

}