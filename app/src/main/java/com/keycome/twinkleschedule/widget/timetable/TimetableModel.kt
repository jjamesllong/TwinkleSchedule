package com.keycome.twinkleschedule.widget.timetable

import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.Section

data class TimetableModel(
    val sectionList: List<Section>,
    val dayList: List<Day>,
    val courseList: List<Course>
)
