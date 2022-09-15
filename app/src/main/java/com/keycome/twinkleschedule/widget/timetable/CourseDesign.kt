package com.keycome.twinkleschedule.widget.timetable

data class CourseDesign(
    val id: Long,
    val day: Int,
    val sectionStart: Int,
    val sectionEnd: Int,
    val text: String,
    val color: Int,
    val textColor: Int
)