package com.keycome.twinkleschedule.database

import com.keycome.twinkleschedule.model.horizon.Date
import com.keycome.twinkleschedule.model.horizon.Day
import com.keycome.twinkleschedule.model.horizon.Time

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

        val schedule = ScheduleEntity(
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
            section = Section(1, 2),
            week = Week(1, 18),
            continuity = Continuity.Continuous,
            teacher = "",
            classroom = ""
        )
        val courseArray = arrayOf(
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "马克思主义基本原理概论",
                day = Day.Monday,
                section = Section(1, 2),
                week = Week(3, 14),
                continuity = Continuity.Continuous,
                teacher = "袁夫友",
                classroom = "文通205"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "有氧健身俱乐部",
                day = Day.Monday,
                section = Section(3, 4),
                week = Week(1, 16),
                continuity = Continuity.Continuous,
                teacher = "陆守芹",
                classroom = "第二田径场(东)"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "信号与系统",
                day = Day.Monday,
                section = Section(5, 7),
                week = Week(3, 15),
                continuity = Continuity.Continuous,
                teacher = "施彩平",
                classroom = "文华103"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "海洋世界",
                day = Day.Monday,
                section = Section(8, 9),
                week = Week(3, 14),
                continuity = Continuity.Continuous,
                teacher = "苏振霞",
                classroom = "文华105"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "电磁场与电磁波",
                day = Day.Tuesday,
                section = Section(1, 2),
                week = Week(4, 14),
                continuity = Continuity.Continuous,
                teacher = "张之光",
                classroom = "主楼208"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "大学英语(四)",
                day = Day.Tuesday,
                section = Section(3, 4),
                week = Week(3, 14),
                continuity = Continuity.Continuous,
                teacher = "刘伟",
                classroom = "文通119"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数字电子技术B",
                day = Day.Tuesday,
                section = Section(5, 7),
                week = Week(3, 15),
                continuity = Continuity.Continuous,
                teacher = "栾晓东",
                classroom = "文华102"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "马克思主义基本原理概论",
                day = Day.Wednesday,
                section = Section(1, 2),
                week = Week(3, 14),
                continuity = Continuity.Continuous,
                teacher = "袁夫友",
                classroom = "文通205"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数值计算",
                day = Day.Wednesday,
                section = Section(3, 4),
                week = Week(3, 10),
                continuity = Continuity.Continuous,
                teacher = "李步军",
                classroom = "文通213"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "形势与政策",
                day = Day.Wednesday,
                section = Section(5, 6),
                week = Week(8, 10),
                continuity = Continuity.Even,
                teacher = "丁小青",
                classroom = "文通302"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "信号与系统",
                day = Day.Thursday,
                section = Section(1, 2),
                week = Week(3, 15),
                continuity = Continuity.Continuous,
                teacher = "施彩平",
                classroom = "文华103"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "电磁场与电磁波",
                day = Day.Thursday,
                section = Section(3, 4),
                week = Week(3, 14),
                continuity = Continuity.Continuous,
                teacher = "张之光",
                classroom = "主楼208"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "大学英语(四)",
                day = Day.Thursday,
                section = Section(5, 6),
                week = Week(3, 14),
                continuity = Continuity.Continuous,
                teacher = "刘伟",
                classroom = "文通119"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数值计算",
                day = Day.Friday,
                section = Section(1, 2),
                week = Week(3, 10),
                continuity = Continuity.Continuous,
                teacher = "李步军",
                classroom = "文通213"
            ),
            Course(
                courseId = 0,
                parentScheduleId = 0,
                title = "数字电子技术B",
                day = Day.Friday,
                section = Section(3, 4),
                week = Week(3, 15),
                continuity = Continuity.Continuous,
                teacher = "栾晓东",
                classroom = "文华102"
            )
        )
    }
}