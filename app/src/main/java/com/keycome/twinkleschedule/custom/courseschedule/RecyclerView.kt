package com.keycome.twinkleschedule.custom.courseschedule

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.toCourseTable() = CourseTable(recyclerView = this)

fun RecyclerView.toSectionTable() = SectionTable(recyclerView = this)

fun RecyclerView.toDayTable() = DayTable(recyclerView = this)