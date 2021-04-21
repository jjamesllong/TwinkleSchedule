package com.keycome.twinkleschedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_schedule")
data class CourseSchedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    var scheduleId: Int,

    var name: String,

    @ColumnInfo(name = "school_begin_date")
    var schoolBeginDate: Date,

    var courses: Int,

    var duration: Int,

    @ColumnInfo(name = "time_line")
    var timeLine: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CourseSchedule

        if (scheduleId != other.scheduleId) return false
        if (name != other.name) return false
        if (schoolBeginDate != other.schoolBeginDate) return false
        if (courses != other.courses) return false
        if (duration != other.duration) return false
        if (!timeLine.contentEquals(other.timeLine)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = scheduleId
        result = 31 * result + name.hashCode()
        result = 31 * result + schoolBeginDate.hashCode()
        result = 31 * result + courses
        result = 31 * result + duration
        result = 31 * result + timeLine.contentHashCode()
        return result
    }
}

data class Date(val year: Int, val month: Int, val dayOfMonth: Int) {
    init {
        if (year !in 1970..9999)
            throw IllegalArgumentException("the format of parameter year is incorrect")
        if (month !in 1..12)
            throw IllegalArgumentException("parameter month should between [1, 12]")
        if (dayOfMonth !in 1..31)
            throw IllegalArgumentException("parameter dayOfYear should between [1, 31]")
    }
}