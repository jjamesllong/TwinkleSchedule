package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.record.timetable.TimeLine
import com.keycome.twinkleschedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditScheduleViewModel : BaseViewModel() {

    private val _liveScheduleName by sharePostVariable(sharedScheduleName) {
        MutableLiveData("新课表")
    }
    val liveScheduleName: LiveData<String> get() = _liveScheduleName

    private val _liveSchoolBeginDate by sharePostVariable(sharedSchoolBeginDate) {
        MutableLiveData(Date.currentDate())
    }
    val liveSchoolBeginDate: LiveData<Date> get() = _liveSchoolBeginDate

    private val _liveDailyCourses by sharePostVariable(sharedDailyCourses) {
        MutableLiveData(10)
    }
    val liveDailyCourses: LiveData<Int> get() = _liveDailyCourses

    private val _liveEndDay by sharePostVariable(sharedEndDay) {
        MutableLiveData(Day.Friday)
    }
    val liveEndDay: LiveData<Day> get() = _liveEndDay

    private val _liveCourseDuration by sharePostVariable(sharedCourseDuration) {
        MutableLiveData(45)
    }
    val liveCourseDuration: LiveData<Int> get() = _liveCourseDuration

    private val _liveTimeLine by sharePostVariable(sharedTimeLine) {
        MutableLiveData(
            mutableSetOf(
                TimeLine(
                    id = System.currentTimeMillis(),
                    name = "默认作息",
                    startDate = _liveSchoolBeginDate.value!!,
                    TestData.getTimeList()
                )
            )
        )
    }
    val liveTimeLine: LiveData<MutableSet<TimeLine>> get() = _liveTimeLine

    var isModify = false

    var modifyingScheduleId = 0L

    override fun onRemove() {
        super.onRemove()
        release(
            sharedScheduleName,
            sharedSchoolBeginDate,
            sharedDailyCourses,
            sharedEndDay,
            sharedCourseDuration,
            sharedTimeLine,
        )
    }

    fun querySchedule(id: Long) {
        viewModelScope.launch {
            val schedule = ScheduleRepository.querySchedule(id)
            _liveScheduleName.value = schedule.name
            _liveSchoolBeginDate.value = schedule.schoolBeginDate
            _liveDailyCourses.value = schedule.dailyCourses
            _liveEndDay.value = schedule.weeklyEndDay
            _liveCourseDuration.value = schedule.courseDuration
            _liveTimeLine.value = schedule.timeLine.toMutableSet()
        }
    }

    fun refreshSchoolBeginDate(date: Date) {
        _liveSchoolBeginDate.value = date
    }

    fun requestNewTimeLine(): TimeLine {
        return TimeLine(
            id = System.currentTimeMillis(),
            name = "新作息",
            startDate = _liveSchoolBeginDate.value!!,
            TestData.getTimeList()
        )
    }

    fun checkScheduleRight(): Boolean {
        if (_liveScheduleName.value.isNullOrBlank())
            return false
        return true
    }

    suspend fun insertSchedule(display: Boolean) {
        val id = if (modifyingScheduleId != 0L) modifyingScheduleId else System.currentTimeMillis()
        val schedule = Schedule(
            scheduleId = id,
            name = _liveScheduleName.value!!,
            schoolBeginDate = _liveSchoolBeginDate.value!!,
            dailyCourses = _liveDailyCourses.value!!,
            weeklyEndDay = _liveEndDay.value!!,
            courseDuration = _liveCourseDuration.value!!,
            timeLine = _liveTimeLine.value!!
        )
        withContext(NonCancellable) {
            if (isModify) {
                ScheduleRepository.updateSchedule(schedule)
            } else {
                ScheduleRepository.insertSchedule(schedule)
            }
            if (display) {
                withContext(Dispatchers.Main) {
                    GlobalPreference.displayScheduleId.value = schedule.scheduleId
                }
            }
        }
    }

    companion object {

        const val sharedScheduleName = "shared_schedule_name"
        const val sharedSchoolBeginDate = "shared_school_begin_date"
        const val sharedDailyCourses = "shared_daily_coursed"
        const val sharedEndDay = "shared_end_day"
        const val sharedCourseDuration = "shared_course_duration"
        const val sharedTimeLine = "shared_time_line"
    }
}