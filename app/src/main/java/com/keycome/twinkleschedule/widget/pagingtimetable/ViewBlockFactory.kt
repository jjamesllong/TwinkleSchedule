package com.keycome.twinkleschedule.widget.pagingtimetable

import com.keycome.twinkleschedule.record.ViewBlock
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Course

object ViewBlockFactory {

    fun produceViewBlock(column: Int, courseList: List<Course>): List<ViewBlock> {
        val viewBlockList = mutableListOf<ViewBlock>()
        var day = Day.Monday.toNumber()
        for (i in courseList.indices) {
            val c = courseList[i]
            val cDay = c.day
            if (i == 0) {
                if (cDay == day) {
                    if (c.section.first() == 1) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = c.section.first() - 1
                        )
                        viewBlockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    }
                } else {
                    val spanDay = cDay - day
                    for (s in 0 until spanDay) {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = column
                        )
                        viewBlockList.add(p)
                    }
                    if (c.section.first() == 1) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = c.section.first() - 1
                        )
                        viewBlockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    }
                    day = cDay
                }
            } else {
                val cBefore = courseList[i - 1]
                if (cDay == day) {
                    val spanCourse = c.section.first() - cBefore.section.last() - 1
                    if (spanCourse == 0) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = spanCourse
                        )
                        viewBlockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    }
                } else {
                    val spanEnd = column - cBefore.section.last()
                    val spanDay = cDay - day - 1
                    if (spanEnd > 0) {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = spanEnd
                        )
                        viewBlockList.add(p)
                    }
                    for (s in 0 until spanDay) {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = column
                        )
                        viewBlockList.add(p)
                    }
                    if (c.section.first() == 1) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = c.section.first() - 1
                        )
                        viewBlockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        viewBlockList.add(b)
                    }
                    day = cDay
                }
            }
        }
        return viewBlockList
    }
}