package com.keycome.twinkleschedule.core.record.timetable

data class Schedule(
    val id: Long,
    val name: String,
    val startDate: String,
    val sectionsPerDay: Int,
    val daysPerWeek: Int,
    val weeksPerTerm: Int,
)
