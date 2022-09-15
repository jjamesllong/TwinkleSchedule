package com.keycome.twinkleschedule.record.timetable

import androidx.room.ColumnInfo

data class CourseAppearance(
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

    val color: String,

    @ColumnInfo(name = "text_color")
    val textColor: String
)