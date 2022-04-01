package com.keycome.twinkleschedule.presentation.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.LiveCourseList
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.CourseField
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel : ViewModel() {
    val liveScheduleList: LiveData<List<Schedule>> = CourseScheduleRepository.queryAllSchedule()

    private var _currentScheduleId: Long = 0L
    val currentScheduleId get() = _currentScheduleId

    private var _liveEditingCourse: LiveCourseList? = null
    val liveEditingCourse get() = _liveEditingCourse!!

    private var _liveQueriedCourse: LiveData<List<Course>>? = null
    val liveQueriedCourse get() = _liveQueriedCourse!!

    fun requestQueriedCourse(scheduleId: Long): LiveData<List<Course>> {
        return if (_currentScheduleId == scheduleId && _liveQueriedCourse != null)
            _liveQueriedCourse!!
        else
            CourseScheduleRepository.queryCourseOfParent(scheduleId).also {
                _currentScheduleId = scheduleId
                _liveQueriedCourse = it
            }
    }

    fun deleteQueriedCourse(course: Course) {
        App.applicationScope.launch {
            CourseScheduleRepository.deleteCourse(course)
        }
    }

    fun insertEditingCourse() {
        App.applicationScope.launch(Dispatchers.IO) {
            val insertable: Boolean = liveEditingCourse.checkFieldRight()
            if (insertable)
                for (c in liveEditingCourse.value) {
                    CourseScheduleRepository.insertCourse(c)
                }
        }
    }

    fun initEditingCourseList(parentScheduleId: CourseField.ParentScheduleId) {
        this._liveEditingCourse = LiveCourseList(parentScheduleId)
    }

    fun clearEditingCourseList() {
        this._liveEditingCourse = null
    }
}