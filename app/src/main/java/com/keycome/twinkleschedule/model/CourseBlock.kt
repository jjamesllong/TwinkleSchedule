package com.keycome.twinkleschedule.model

data class CourseBlock(
    val isCourse: Boolean,
    val spanSize: Int,
    val courseIndex: Int = -1
)