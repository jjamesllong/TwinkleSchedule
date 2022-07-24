package com.keycome.twinkleschedule.record.timetable

data class TimetableDescriber(
    val schedule: Schedule,
    val dailyRoutines: List<DailyRoutine>,
    val courses: List<Course>
)