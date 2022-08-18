package com.keycome.twinkleschedule.record.timetable

import androidx.room.ColumnInfo
import com.keycome.twinkleschedule.record.interval.Day

data class CourseAppearance(
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

    val color: String
)