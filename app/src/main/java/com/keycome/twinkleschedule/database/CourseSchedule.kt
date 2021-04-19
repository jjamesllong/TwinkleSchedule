package com.keycome.twinkleschedule.database

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class CourseSchedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    var scheduleId: Int,
    var name: String,
    var schoolBeginDate: String,
    var courses: Int,
    var duration: Int,
    var timeLine: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CourseSchedule

        if (name != other.name) return false
        if (schoolBeginDate != other.schoolBeginDate) return false
        if (courses != other.courses) return false
        if (duration != other.duration) return false
        if (!timeLine.contentEquals(other.timeLine)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + schoolBeginDate.hashCode()
        result = 31 * result + courses
        result = 31 * result + duration
        result = 31 * result + timeLine.contentHashCode()
        return result
    }
}