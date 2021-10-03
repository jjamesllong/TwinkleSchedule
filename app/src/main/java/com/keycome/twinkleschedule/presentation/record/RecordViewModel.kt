package com.keycome.twinkleschedule.presentation.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.model.LiveCourseList
import com.keycome.twinkleschedule.model.sketch.CourseField
import com.keycome.twinkleschedule.model.sketch.Schedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel : ViewModel() {
    val liveScheduleList: LiveData<List<Schedule>> = CourseScheduleRepository.queryAllSchedule()

    private var _liveCourseList: LiveCourseList? = null
    val liveCourseList get() = _liveCourseList!!

    fun insertCourse() {
        App.applicationScope.launch(Dispatchers.IO) {
            val insertable: Boolean = liveCourseList.checkFieldRight()
            if (insertable)
                for (c in liveCourseList.value) {
                    CourseScheduleRepository.insertCourse(c)
                }
        }
    }

    fun initLiveCourseList(parentScheduleId: CourseField.ParentScheduleId) {
        this._liveCourseList = LiveCourseList(parentScheduleId)
    }

    fun clearLiveCourseList() {
        this._liveCourseList = null
    }
}