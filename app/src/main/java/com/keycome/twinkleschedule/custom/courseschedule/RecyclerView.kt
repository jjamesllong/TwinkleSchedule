package com.keycome.twinkleschedule.custom.courseschedule

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.Schedule

fun RecyclerView.toScheduleTable(schedule: Schedule, courseList: List<Course>) {
    val viewBlockList = ViewBlockFactory.convertEntityToBlock(schedule, courseList)
    val gridLayoutManager = GridLayoutManager(
        context,
        schedule.dailyCourses,
        GridLayoutManager.HORIZONTAL,
        false
    ).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return viewBlockList[position].spanSize
            }
        }
    }
    val courseAdapter = CourseAdapter(
        daySpan = schedule.weeklyEndDay.toNumber(),
        courseList = courseList,
        viewBlockList = viewBlockList
    )
    layoutManager = gridLayoutManager
    adapter = courseAdapter
}