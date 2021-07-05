package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.database.TimeLine

class LiveSchedule(
    schedule: ScheduleEntity = ScheduleEntity(
        scheduleId = 0,
        name = "课表一",
        schoolBeginDate = Date(2021, 3, 1),
        dailyCourses = 10,
        weeklyEndDay = Day.Friday,
        courseDuration = 45,
        timeLine = setOf(TestData.timeLine)
    )
) : LiveData<ScheduleEntity>(schedule) {
    companion object {
        const val schedule_id = -1
        const val name_ = -2
        const val school_begin_date = -3
        const val daily_courses = -4
        const val weekly_end_day = -5
        const val course_duration = -6
        const val time_line = -7
        const val time_line_id = -8
        const val time_line_name = -9
        const val time_line_start_date = -10
        const val time_line_list = -11
    }

    fun updateValue(key: Int, value: Any, timeLineId: Int = -1) {
        val schedule = super.getValue()!!
        super.setValue(
            when (key) {
                schedule_id -> schedule.copy(scheduleId = value as Int)
                name_ -> schedule.copy(name = value as String)
                school_begin_date -> schedule.copy(schoolBeginDate = value as Date)
                daily_courses -> schedule.copy(dailyCourses = value as Int)
                weekly_end_day -> schedule.copy(weeklyEndDay = value as Day)
                course_duration -> schedule.copy(courseDuration = value as Int)
                time_line -> {
                    val v = value as Set<*>
                    val set = mutableSetOf<TimeLine>()
                    v.forEach { set.add(it as TimeLine) }
                    schedule.copy(timeLine = set)
                }
                time_line_name -> {
                    schedule.timeLine.find { it.id == timeLineId }?.let {
                        val timeLine = it.copy(name = value as String)
                        val set = schedule.timeLine.toMutableSet()
                        set.remove(it)
                        set.add(timeLine)
                        schedule.copy(timeLine = set)
                    } ?: schedule
                }
                else -> throw IllegalArgumentException("the key $key is not defined in LiveSchedule")
            }
        )
    }

    override fun setValue(value: ScheduleEntity) {
        super.setValue(value)
    }

    override fun getValue(): ScheduleEntity {
        return super.getValue()!!
    }
}