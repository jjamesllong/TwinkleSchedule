package com.keycome.twinkleschedule.database

class TestData {
    companion object {
        private val timeLineArray = arrayOf(
            "08:00", "08:55", "10:00", "10:55", "14:00", "14:55", "15:50", "16:45", "17:40", "19:00"
        )
        val courseSchedule = CourseSchedule(
            scheduleId = 0,
            name = "SecondSemesterOfSophomoreYear",
            schoolBeginDate = Date(2021, 3, 1),
            courses = timeLineArray.size,
            duration = 45,
            timeLine = timeLineArray
        )
    }
}