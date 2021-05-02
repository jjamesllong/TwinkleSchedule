package com.keycome.twinkleschedule.presentation.display

import com.keycome.twinkleschedule.database.Day
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.model.CourseBlock

class BlockFactory {
    private val blockList = mutableListOf<CourseBlock>()

    fun convertEntityToBlock(): Array<CourseBlock> {
        val schedule = TestData.courseSchedule
        val array = TestData.courseArray
        var day = convertDayToNum(Day.Monday)
        for (i in array.indices) {
            val c = array[i]
            val cDay = convertDayToNum(c.day)
            if (i == 0) {
                if (cDay == day) {
                    if (c.section.startSection == 1) {
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = CourseBlock(
                            isPlaceHolder = true,
                            spanSize = c.section.startSection - 1
                        )
                        blockList.add(p)
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
                            courseIndex = i
                        )
                        blockList.add(b)
                    }
                } else {
                    val spanDay = cDay - day - 1
                    for (s in 0 until spanDay) {
                        val p = CourseBlock(
                            isPlaceHolder = true,
                            spanSize = schedule.courses
                        )
                        blockList.add(p)
                    }
                    if (c.section.startSection == 1) {
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = CourseBlock(
                            isPlaceHolder = true,
                            spanSize = c.section.startSection - 1
                        )
                        blockList.add(p)
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
                            courseIndex = i
                        )
                        blockList.add(b)
                    }
                    day = cDay
                }
            } else {
                val cBefore = array[i - 1]
                if (cDay == day) {
                    val spanCourse = c.section.startSection - cBefore.section.endSection
                    if (spanCourse == 1) {
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = CourseBlock(
                            isPlaceHolder = true,
                            spanSize = c.section.startSection - 1
                        )
                        blockList.add(p)
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
                            courseIndex = i
                        )
                        blockList.add(b)
                    }
                } else {
                    val spanEnd = schedule.courses - cBefore.section.endSection
                    val spanDay = cDay - day - 1
                    if (spanEnd > 0) {
                        val p = CourseBlock(
                            isPlaceHolder = true,
                            spanSize = spanEnd
                        )
                        blockList.add(p)
                    }
                    for (s in 0 until spanDay) {
                        val p = CourseBlock(
                            isPlaceHolder = true,
                            spanSize = schedule.courses
                        )
                        blockList.add(p)
                    }
                    if (c.section.startSection == 1) {
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
                            courseIndex = i
                        )
                        blockList.add(b)
                    } else {
                        val p = CourseBlock(
                            isPlaceHolder = true,
                            spanSize = c.section.startSection - 1
                        )
                        blockList.add(p)
                        val b = CourseBlock(
                            isPlaceHolder = false,
                            spanSize = c.section.endSection - c.section.startSection + 1,
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