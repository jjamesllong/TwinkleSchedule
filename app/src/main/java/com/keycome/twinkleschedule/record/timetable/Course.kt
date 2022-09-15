package com.keycome.twinkleschedule.record.timetable

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course")
data class Course(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "course_id")
    val courseId: Long,

    @ColumnInfo(name = "master_id")
    val masterId: Long,

    val title: String,

    val day: Int,

    val section: List<Int>,

    val week: List<Int>,

    val teacher: String,

    val classroom: String,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        courseId = parcel.readLong(),
        masterId = parcel.readLong(),
        title = parcel.readString() ?: throw Exception(),
        day = parcel.readInt(),
        section = IntArray(parcel.readInt()) { parcel.readInt() }.toList(),
        week = IntArray(parcel.readInt()) { parcel.readInt() }.toList(),
        teacher = parcel.readString() ?: throw Exception(),
        classroom = parcel.readString() ?: throw Exception()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(courseId)
        parcel.writeLong(masterId)
        parcel.writeString(title)
        parcel.writeInt(day)
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

    fun toDesignText(): String = StringBuilder()
        .append(title)
        .append("\n@")
        .append(classroom)
        .toString()

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<Course> {

            override fun createFromParcel(parcel: Parcel): Course = Course(parcel)

            override fun newArray(size: Int): Array<Course?> = arrayOfNulls(size)

        }
    }

}