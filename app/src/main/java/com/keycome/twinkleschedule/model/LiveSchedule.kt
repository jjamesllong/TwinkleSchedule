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

    fun updateValue(key: Int, value: Any) {
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
                else -> throw IllegalArgumentException("the key $key is not defined to update liveSchedule")
            }
        )
    }

    fun updateTimeLineName(timeLineId: Int, name: String) {
        super.setValue(
            actionToTimeLine(super.getValue()!!, timeLineId) { it.copy(name = name) }
        )
    }

    fun updateTimeLineDate(timeLineId: Int, date: Date) {
        super.setValue(
            actionToTimeLine(super.getValue()!!, timeLineId) { it.copy(startDate = date) }
        )
    }

    fun removeTimeLineListElement(timeLineId: Int, index: Int) {
        super.setValue(
            actionToTimeLine(super.getValue()!!, timeLineId) {
                val list = it.timeLineList.toMutableList()
                list.removeAt(index)
                it.copy(timeLineList = list)
            }
        )
    }

    fun addOrUpdateTimeLineListElement(timeLineId: Int, time: Time, index: Int = -1): Boolean {
        var result = false
        super.setValue(
            actionToTimeLine(super.getValue()!!, timeLineId) {
                val list = it.timeLineList.toMutableList()
                result = insertTime(time, list, index)
                it.copy(timeLineList = list)
            }
        )
        return result
    }

    private inline fun actionToTimeLine(
        schedule: ScheduleEntity,
        timeLineId: Int,
        action: (TimeLine) -> TimeLine
    ): ScheduleEntity {
        return schedule.timeLine.find { it.id == timeLineId }?.let {
            val timeLine = action(it)
            val set = schedule.timeLine.toMutableSet()
            set.remove(it)
            set.add(timeLine)
            schedule.copy(timeLine = set)
        } ?: schedule
    }

    private fun insertTime(time: Time, list: MutableList<Time>, index: Int): Boolean {
        val t = time.hour * 60 + time.minute
        val last = list.size
        for (i in 0..last) {
            val tFront = when (i) {
                0 -> 0
                else -> list[i - 1].hour * 60 + list[i - 1].minute
            }
            val tBehind = when (i) {
                last -> 1440
                else -> list[i].hour * 60 + list[i].minute
            }
            if (t in (tFront + 1) until tBehind) {
                if (index >= 0) {
                    when {
                        i < index -> {
                            list.removeAt(index)
                            list.add(i, time)
                        }
                        i == index -> list[index] = time
                        i > index -> {
                            list.removeAt(index)
                            if (i == last) list.add(time)
                            else list.add(i - 1, time)
                        }
                    }
                } else {
                    when (i) {
                        last -> list.add(time)
                        else -> list.add(i, time)
                    }
                }
                return true
            }
        }
        return false
    }

    override fun setValue(value: ScheduleEntity) {
        super.setValue(value)
    }

    override fun getValue(): ScheduleEntity {
        return super.getValue()!!
    }
}