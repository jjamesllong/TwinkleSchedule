package com.keycome.twinkleschedule.core.record.timetable

data class Zone(
    val id: Long,
    val masterRoutineId: Long,
    val position: Int,
    val startSecondInDay: Int,
    val endSecondInDay: Int,
)
