package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseViewModel2
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.sketch.Schedule
import com.keycome.twinkleschedule.record.sketch.TimeLine
import com.keycome.twinkleschedule.record.span.Date
import com.keycome.twinkleschedule.record.span.Day
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class EditScheduleViewModel : BaseViewModel2() {

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

    override fun onCleared() {
        super.onCleared()
        release(
            sharedScheduleName,
            sharedSchoolBeginDate,
            sharedDailyCourses,
            sharedEndDay,
            sharedCourseDuration,
            sharedTimeLine,
        )
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
        val schedule = Schedule(
            scheduleId = System.currentTimeMillis(),
            name = _liveScheduleName.value!!,
            schoolBeginDate = _liveSchoolBeginDate.value!!,
            dailyCourses = _liveDailyCourses.value!!,
            weeklyEndDay = _liveEndDay.value!!,
            courseDuration = _liveCourseDuration.value!!,
            timeLine = _liveTimeLine.value!!
        )
        withContext(NonCancellable) {
            CourseScheduleRepository.insertSchedule(schedule)
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