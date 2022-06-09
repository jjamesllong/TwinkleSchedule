package com.keycome.twinkleschedule.widget.pagingtimetable

import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.record.timetable.Course

fun RecyclerView.toTimetableBody(
    courseSelectedListener: (Course) -> Unit
) = TimetableBody(recyclerView = this, courseSelectedListener)

fun RecyclerView.toSectionBar() = TimetableSectionBar(recyclerView = this)

fun RecyclerView.toDayBar() = TimetableDayBar(recyclerView = this)