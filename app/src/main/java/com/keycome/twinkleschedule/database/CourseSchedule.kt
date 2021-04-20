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
    var schoolBeginDate: String,

    var courses: Int,

    var duration: Int,

    @ColumnInfo(name = "time_line")
    var timeLine: String
)