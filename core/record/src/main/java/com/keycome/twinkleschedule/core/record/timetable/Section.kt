package com.keycome.twinkleschedule.core.record.timetable

data class Section(
    val id: Long,
    val masterCourseId: Long,
    val teacher: String,
    val classroom: String,
    val startOfDay: Int,
    val endOfDay: Int,
    val dayOfWeek: Int,
    val weekOfTerm: Int,
)
