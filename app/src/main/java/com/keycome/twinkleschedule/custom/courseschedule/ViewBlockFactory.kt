package com.keycome.twinkleschedule.custom.courseschedule

import com.keycome.twinkleschedule.record.ViewBlock
import com.keycome.twinkleschedule.record.span.Day
import com.keycome.twinkleschedule.record.sketch.Course
import com.keycome.twinkleschedule.record.sketch.Schedule

object ViewBlockFactory {

    fun selectToGroupAndSort(sourceList: List<Course>, targetWeek: Int): List<Course> {
        val unselectedList = mutableListOf<Course>()
        sourceList.forEach {
            if (it.week.contains(targetWeek))
                unselectedList.add(it)
        }
        val unorderedMap: Map<Day, List<Course>> = unselectedList.groupBy { it.day }
        val unorderedList = mutableListOf<MutableList<Course>>()
        for (d in 0 until 7) {
            unorderedMap.forEach {
                if (it.key.ordinal == d)
                    unorderedList.add(it.value.toMutableList())
            }
        }
        unorderedList.forEach { list ->
            val l = list.size
            for (i in 0 until l - 1) {
                for (j in 0 until l - 1 - i) {
                    if (list[j].section.first() > list[j + 1].section.first()) {
                        val temp = list[j]
                        list[j] = list[j + 1]
                        list[j + 1] = temp
                    }
                }
            }
        }
        val resultList = mutableListOf<Course>()
        unorderedList.forEach { l ->
            l.forEach { c ->
                resultList.add(c)
            }
        }
        return resultList
    }

    fun produceViewBlock(
        schedule: Schedule, courseList: List<Course>
    ): List<ViewBlock> {
        val viewBlockList = mutableListOf<ViewBlock>()
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
                            spanSize = schedule.dailyCourses
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
                    val spanEnd = schedule.dailyCourses - cBefore.section.last()
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
                            spanSize = schedule.dailyCourses
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