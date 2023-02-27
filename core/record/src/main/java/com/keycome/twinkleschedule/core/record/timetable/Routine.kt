package com.keycome.twinkleschedule.core.record.timetable

data class Routine(
    val id: Long,
    val masterScheduleId: Long,
    val name: String,
    val startDate: String,
)
