package com.keycome.twinkleschedule.custom.courseschedule

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun RecyclerView.toCourseTable(
    lifecycleOwner: LifecycleOwner,
    schedule: Schedule,
    courseList: List<Course>
) {

    lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        val viewBlockList = withContext(Dispatchers.Default) {
            ViewBlockFactory.produceViewBlock(schedule, courseList)
        }

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
}

fun RecyclerView.toSectionTable(schedule: Schedule) {

    val linearLayoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
    }

    val sectionAdapter = SectionAdapter(schedule)

    layoutManager = linearLayoutManager
    adapter = sectionAdapter
}

fun RecyclerView.toDayTable(schedule: Schedule) {

    val linearLayoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.HORIZONTAL
    }

    val dayAdapter = DayAdapter(schedule)

    layoutManager = linearLayoutManager
    adapter = dayAdapter
}