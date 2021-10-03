package com.keycome.twinkleschedule.database

import com.keycome.twinkleschedule.model.horizon.Date
import com.keycome.twinkleschedule.model.horizon.Day
import com.keycome.twinkleschedule.model.horizon.Time
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.Schedule
import com.keycome.twinkleschedule.model.sketch.TimeLine

class TestData {
    companion object {
        private val timeLineList = listOf(
            "08:00", "08:55", "10:00", "10:55", "14:00", "14:55", "15:50", "16:45", "17:40", "19:00"
        )

        fun getTimeList(): List<Time> {
            val l = mutableListOf<Time>()
            for (t in timeLineList) {
                l.add(
                    Time(
                        t.substring(0..1).toInt(),
                        t.substring(3..4).toInt(),
                        0
                    )
                )
            }
            return l.toList()
        }

        val timeLine = TimeLine(
            0,
            "test",
            Date(2021, 3, 1),
            getTimeList()
        )

        val schedule = Schedule(
            scheduleId = 0,
            name = "SecondSemesterOfSophomoreYear",
            schoolBeginDate = Date(2021, 3, 1),
            dailyCourses = timeLineList.size,
            weeklyEndDay = Day.Friday,
            courseDuration = 45,
            timeLine = setOf()
        )
        private val course = Course(
            courseId = 0,
            parentScheduleId = 0,
            title = "",
            day = Day.Monday,
            // section = Section(1, 2),
            section = (1..2).toList(),
            // week = Week(1, 18),
            week = (1..18).toList(),
            // continuity = Continuity.Continuous,
            teacher = "",
            classroom = ""
        )
        val courseList = listOf(
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "马克思主义基本原理概论",
                day = Day.Monday,
                section = (1..2).toList(),
                week = (3..14).toList(),
                teacher = "袁夫友",
                classroom = "文通205"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "有氧健身俱乐部",
                day = Day.Monday,
                section = (3..4).toList(),
                week = (1..16).toList(),
                teacher = "陆守芹",
                classroom = "第二田径场(东)"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "信号与系统",
                day = Day.Monday,
                section = (5..7).toList(),
                week = (3..15).toList(),
                teacher = "施彩平",
                classroom = "文华103"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "海洋世界",
                day = Day.Monday,
                section = (8..9).toList(),
                week = (3..14).toList(),
                teacher = "苏振霞",
                classroom = "文华105"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "电磁场与电磁波",
                day = Day.Tuesday,
                section = (1..2).toList(),
                week = (4..14).toList(),
                teacher = "张之光",
                classroom = "主楼208"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "大学英语(四)",
                day = Day.Tuesday,
                section = (3..4).toList(),
                week = (3..14).toList(),
                teacher = "刘伟",
                classroom = "文通119"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数字电子技术B",
                day = Day.Tuesday,
                section = (5..7).toList(),
                week = (3..15).toList(),
                teacher = "栾晓东",
                classroom = "文华102"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "马克思主义基本原理概论",
                day = Day.Wednesday,
                section = (1..2).toList(),
                week = (3..14).toList(),
                teacher = "袁夫友",
                classroom = "文通205"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数值计算",
                day = Day.Wednesday,
                section = (3..4).toList(),
                week = (3..10).toList(),
                teacher = "李步军",
                classroom = "文通213"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "形势与政策",
                day = Day.Wednesday,
//                section = Section(5, 6),
//                week = Week(8, 10),
//                continuity = Continuity.Even,
                section = (5..6).toList(),
                week = (8..10).step(step = 2).toList(),
                teacher = "丁小青",
                classroom = "文通302"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "信号与系统",
                day = Day.Thursday,
                section = (1..2).toList(),
                week = (3..15).toList(),
                teacher = "施彩平",
                classroom = "文华103"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "电磁场与电磁波",
                day = Day.Thursday,
                section = (3..4).toList(),
                week = (3..14).toList(),
                teacher = "张之光",
                classroom = "主楼208"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "大学英语(四)",
                day = Day.Thursday,
                section = (5..6).toList(),
                week = (3..14).toList(),
                teacher = "刘伟",
                classroom = "文通119"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数值计算",
                day = Day.Friday,
                section = (1..2).toList(),
                week = (3..10).toList(),
                teacher = "李步军",
                classroom = "文通213"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数字电子技术B",
                day = Day.Friday,
                section = (3..4).toList(),
                week = (3..15).toList(),
                teacher = "栾晓东",
                classroom = "文华102"
            )
        )
    }
}