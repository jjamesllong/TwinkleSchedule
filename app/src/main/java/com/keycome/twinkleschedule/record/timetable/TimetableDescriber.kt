package com.keycome.twinkleschedule.record.timetable

data class TimetableDescriber(
    val schedule: Schedule,
    val routines: List<Routine>,
    val courses: List<CourseAppearance>
)