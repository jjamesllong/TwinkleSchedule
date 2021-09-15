package com.keycome.twinkleschedule.presentation.display

import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.model.ViewBlock
import com.keycome.twinkleschedule.model.horizon.Day

class ViewBlockFactory {
    private val blockList = mutableListOf<ViewBlock>()

    fun convertEntityToBlock(): Array<ViewBlock> {
        val schedule = TestData.schedule
        val array = TestData.courseArray
        var day = convertDayToNum(Day.Monday)
        for (i in array.indices) {
            val c = array[i]
            val cDay = convertDayToNum(c.day)
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
                val cBefore = array[i - 1]
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
        return blockList.toTypedArray()
    }

    private fun convertDayToNum(day: Day) = when (day) {
        Day.Monday -> 1
        Day.Tuesday -> 2
        Day.Wednesday -> 3
        Day.Thursday -> 4
        Day.Friday -> 5
        Day.Saturday -> 6
        Day.Sunday -> 7
    }
}