package com.keycome.twinkleschedule.custom.courseschedule

import com.keycome.twinkleschedule.model.ViewBlock
import com.keycome.twinkleschedule.model.horizon.Day
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.Schedule

object ViewBlockFactory {

    fun convertEntityToBlock(
        schedule: Schedule, courseList: List<Course>
    ): List<ViewBlock> {
        val blockList = mutableListOf<ViewBlock>()
        var day = Day.Monday.toNumber()
        for (i in courseList.indices) {
            val c = courseList[i]
            val cDay = c.day.toNumber()
            if (i == 0) {
                if (cDay == day) {
                    if (c.section.first() == 1) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = c.section.first() - 1
                        )
                        blockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    }
                } else {
                    val spanDay = cDay - day - 1
                    for (s in 0 until spanDay) {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = schedule.dailyCourses
                        )
                        blockList.add(p)
                    }
                    if (c.section.first() == 1) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = c.section.first() - 1
                        )
                        blockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    }
                    day = cDay
                }
            } else {
                val cBefore = courseList[i - 1]
                if (cDay == day) {
                    val spanCourse = c.section.first() - cBefore.section.last()
                    if (spanCourse == 1) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = c.section.first() - 1
                        )
                        blockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    }
                } else {
                    val spanEnd = schedule.dailyCourses - cBefore.section.last()
                    val spanDay = cDay - day - 1
                    if (spanEnd > 0) {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = spanEnd
                        )
                        blockList.add(p)
                    }
                    for (s in 0 until spanDay) {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = schedule.dailyCourses
                        )
                        blockList.add(p)
                    }
                    if (c.section.first() == 1) {
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = ViewBlock(
                            isCourse = false,
                            spanSize = c.section.first() - 1
                        )
                        blockList.add(p)
                        val b = ViewBlock(
                            isCourse = true,
                            spanSize = c.section.size,
                            courseIndex = i
                        )
                        blockList.add(b)
                    }
                    day = cDay
                }
            }
        }
        return blockList
    }
}