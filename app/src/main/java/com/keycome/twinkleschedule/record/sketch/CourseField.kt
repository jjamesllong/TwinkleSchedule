package com.keycome.twinkleschedule.record.sketch

import com.keycome.twinkleschedule.record.horizon.Day

sealed class CourseField {
    data class CourseId(val courseId: Long) : CourseField()
    data class ParentScheduleId(val parentScheduleId: Long) : CourseField()
    data class Title(val title: String) : CourseField()
    data class FDay(val day: Day) : CourseField()
    data class Section(val section: List<Int>) : CourseField()
    data class Week(val week: List<Int>) : CourseField()
    data class Teacher(val teacher: String) : CourseField()
    data class Classroom(val classroom: String) : CourseField()
}
