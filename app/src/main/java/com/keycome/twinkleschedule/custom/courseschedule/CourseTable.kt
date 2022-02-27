package com.keycome.twinkleschedule.custom.courseschedule

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.record.sketch.CourseSchedule

class CourseTable(recyclerView: RecyclerView) : RecyclerTable<CourseSchedule>() {

    private val reverseLayout = false

    private val courseAdapter = CourseAdapter()

    init {
        super.recyclerView = recyclerView
        configureRecyclerView {
            adapter = courseAdapter.also { super.tableAdapter = it }
        }
    }

    override fun submitData(data: CourseSchedule) {
        val viewBlockList = ViewBlockFactory.produceViewBlock(data.schedule, data.courseList)
        configureRecyclerView {
            layoutManager = GridLayoutManager(
                context,
                data.schedule.dailyCourses,
                GridLayoutManager.HORIZONTAL,
                reverseLayout
            ).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return viewBlockList[position].spanSize
                    }
                }
            }
        }
        courseAdapter.daySpan = data.schedule.weeklyEndDay.toNumber()
        courseAdapter.viewBlockList = viewBlockList
        courseAdapter.schedule = data.schedule
        courseAdapter.courseList = data.courseList
        super.submitData(data)
    }
}