package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel2
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.sketch.Course
import com.keycome.twinkleschedule.record.sketch.CourseSchedule
import com.keycome.twinkleschedule.record.sketch.Schedule
import com.keycome.twinkleschedule.record.span.SpanDifference
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayCoursesViewModel : BaseViewModel2() {

    private val _displayScheduleId = GlobalPreference.displayScheduleId
    val liveDisplayScheduleId get() = _displayScheduleId

    private val _liveParentSchedule = MutableLiveData<Schedule>()
    val liveParentSchedule: LiveData<Schedule> get() = _liveParentSchedule

    private val _liveWeekNow = MutableLiveData<Int>()
    val liveWeekNow: LiveData<Int> get() = _liveWeekNow

    private val _liveWeekSelected = MutableLiveData<Int>()
    val liveWeekSelected: LiveData<Int> get() = _liveWeekSelected

    private val _liveCourseList = MutableLiveData<List<Course>>()
    val liveCourseList: LiveData<List<Course>> get() = _liveCourseList

    private val _liveCourseSchedule = MutableLiveData<CourseSchedule>()
    val liveCourseSchedule: LiveData<CourseSchedule> get() = _liveCourseSchedule

    fun refreshParentSchedule(id: Long) {
        viewModelScope.launch {
            val parentSchedule = withContext(Dispatchers.IO) {
                CourseScheduleRepository.queryScheduleByIdQuietly(id)
            }
            _liveParentSchedule.value = parentSchedule
        }
    }

    fun refreshWeekNow(schedule: Schedule) {
        val weekNow = SpanDifference.weeklyDiff(
            schedule.schoolBeginDate.toMillis(),
            System.currentTimeMillis()
        ) + 1
        _liveWeekNow.value = weekNow
    }

    fun refreshWeekSelected(week: Int) {
        _liveWeekSelected.value = week
    }

    fun refreshCourseList(week: Int) {
        _liveParentSchedule.value?.let { schedule ->
            viewModelScope.launch {
                val courseList = withContext(Dispatchers.IO) {
                    CourseScheduleRepository.queryCourseByParentQuietly(
                        schedule.scheduleId,
                        week
                    )
                }
                _liveCourseList.value = courseList
            }
        }
    }

    fun refreshCourseSchedule() {
        _liveParentSchedule.value?.let { schedule ->
            _liveCourseList.value?.let { courseList ->
                _liveCourseSchedule.value = CourseSchedule(schedule, courseList)
            }
        }
    }
}