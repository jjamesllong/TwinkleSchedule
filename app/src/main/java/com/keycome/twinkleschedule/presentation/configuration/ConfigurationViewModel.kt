package com.keycome.twinkleschedule.presentation.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.database.TimeLine
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.Date
import com.keycome.twinkleschedule.model.Day
import com.keycome.twinkleschedule.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigurationViewModel : ViewModel() {

    companion object {
        const val schedule_id = -1
        const val name_ = -2
        const val school_begin_date = -3
        const val daily_courses = -4
        const val weekly_end_day = -5
        const val course_duration = -6
        const val time_line = -7
    }

    val liveScheduleList: LiveData<List<ScheduleEntity>> by lazy { Repository.queryAllSchedule() }

    private val mutableLiveSchedule: MutableLiveData<ScheduleEntity> by lazy {
        MutableLiveData<ScheduleEntity>(
            ScheduleEntity(
                scheduleId = 0,
                name = "课表一",
                schoolBeginDate = Date(2021, 3, 1),
                dailyCourses = 10,
                weeklyEndDay = Day.Friday,
                courseDuration = 45,
                timeLine = setOf(TestData.timeLine)
            )
        )
    }

    val liveSchedule: LiveData<ScheduleEntity> get() = mutableLiveSchedule

    fun updateLiveSchedule(key: Int, value: Any) {
        val schedule = mutableLiveSchedule.value!!
        when (key) {
            schedule_id -> schedule.scheduleId = value as Int
            name_ -> schedule.name = value as String
            school_begin_date -> schedule.schoolBeginDate = value as Date
            daily_courses -> schedule.dailyCourses = value as Int
            weekly_end_day -> schedule.weeklyEndDay = value as Day
            course_duration -> schedule.courseDuration = value as Int
            time_line -> {
                val v = value as Set<*>
                val set = mutableSetOf<TimeLine>()
                v.forEach { set.add(it as TimeLine) }
                schedule.timeLine = set
            }
        }
        mutableLiveSchedule.value = schedule
    }

    fun insertSchedule() {
        GlobalScope.launch {
            Repository.insertSchedule(liveSchedule.value!!)
            launch(Dispatchers.Main) { toast("数据写入成功") }
        }
    }
}