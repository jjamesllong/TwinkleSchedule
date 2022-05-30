package com.keycome.twinkleschedule.custom.pagingtimetable

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.record.ViewBlock
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.Schedule

class TimetableBody(val recyclerView: RecyclerView, courseSelectedListener: (Course) -> Unit) {

    val adapter = CourseAdapter(courseSelectedListener).also { recyclerView.adapter = it }

    var viewBlockList = listOf<ViewBlock>()

    var verticalSpanCount = 0
        set(param) {
            if (field != param) {
                field = param
                recyclerView.layoutManager = GridLayoutManager(
                    recyclerView.context,
                    param,
                    GridLayoutManager.HORIZONTAL,
                    false
                ).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return viewBlockList[position].spanSize
                        }
                    }
                }
            }
        }

    fun submitData(schedule: Schedule, courseList: List<Course>) {
        viewBlockList = ViewBlockFactory.produceViewBlock(schedule.endSection, courseList)
        verticalSpanCount = schedule.endSection
        adapter.courseList = courseList
        adapter.viewBlockList = viewBlockList
        adapter.horizontalSpanCount = schedule.endDay
        adapter.notifyDataSetChanged()
    }
}