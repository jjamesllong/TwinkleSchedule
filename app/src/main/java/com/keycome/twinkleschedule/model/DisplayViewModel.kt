package com.keycome.twinkleschedule.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.extension.asLiveData
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.record.timetable.TimetableDescriber
import com.keycome.twinkleschedule.repository.CourseRepository
import com.keycome.twinkleschedule.repository.DailyRoutineRepository
import com.keycome.twinkleschedule.repository.ScheduleRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DisplayViewModel : BaseViewModel() {

    private val _liveScheduleId = GlobalPreference.displayScheduleId
    val liveScheduleId = _liveScheduleId.asLiveData()

    private val _liveSchedule = MutableLiveData<Schedule>()
    val liveSchedule = _liveSchedule.asLiveData()

    private val _liveDisplayStatus = MutableLiveData<Int>()
    val liveDisplayStatus = _liveDisplayStatus.asLiveData()

    private val _liveWeekSelected = MutableLiveData<Int>()
    val liveWeekSelected = _liveWeekSelected.asLiveData()

    private val _liveDescriber = MutableLiveData<TimetableDescriber>()
    val liveDescriber = _liveDescriber.asLiveData()

    private val scheduleIdObserver = Observer<Long> { id ->
        viewModelScope.launch {
            val schedule = ScheduleRepository.querySchedule(id)
            if (schedule == null) {
                _liveDisplayStatus.value = NOTHING
            } else {
                _liveSchedule.value = schedule
            }
        }
    }

    private val scheduleObserver = Observer<Schedule> { schedule ->
        _liveDisplayStatus.value = when (val week = schedule.inferWeekNow()) {
            -1 -> UPCOMING
            0 -> ENDED
            else -> week
        }
    }

    private val statusObserver = Observer<Int> { status ->
        if (status != NOTHING) {
            _liveWeekSelected.value = when (status) {
                UPCOMING, ENDED -> 1
                else -> status
            }
            viewModelScope.launch {
                val s = liveSchedule.value!!
                val routines = async { DailyRoutineRepository.queryDailyRoutines(s.scheduleId) }
                val courses = async { CourseRepository.queryCoursesOfSchedule(s.scheduleId) }
                _liveDescriber.value = TimetableDescriber(s, routines.await(), courses.await())
            }
        } else {
            _liveWeekSelected.value = 0
        }
    }

    override suspend fun onPlace() {
        super.onPlace()
        liveScheduleId.observeForever(scheduleIdObserver)
        liveSchedule.observeForever(scheduleObserver)
        liveDisplayStatus.observeForever(statusObserver)
    }

    override fun onRemove() {
        super.onRemove()
        liveScheduleId.removeObserver(scheduleIdObserver)
        liveSchedule.removeObserver(scheduleObserver)
        liveDisplayStatus.removeObserver(statusObserver)
    }

    fun getDisplayStatus(): Int {
        return when (val status = liveDisplayStatus.value ?: NOTHING) {
            NOTHING -> NOTHING
            UPCOMING -> UPCOMING
            ENDED -> ENDED
            else -> status
        }
    }

    fun setWeekSelected(week: Int) {
        val previousWeek = _liveWeekSelected.value ?: 0
        if (previousWeek != week) {
            _liveWeekSelected.value = week
        }
    }

    fun getDisplayCourseById(id: Long): Course? {
        return liveDescriber.value?.courses?.find { it.courseId == id }
    }

    companion object {

        const val NOTHING = 0
        const val UPCOMING = -1
        const val ENDED = -2
    }
}