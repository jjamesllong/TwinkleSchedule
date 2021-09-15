package com.keycome.twinkleschedule.presentation.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.model.LiveCourseList
import com.keycome.twinkleschedule.model.horizon.Day
import com.keycome.twinkleschedule.model.sketch.*
import com.keycome.twinkleschedule.repository.Repository

class RecordViewModel : ViewModel() {
    val liveScheduleList: LiveData<List<Schedule>> = Repository.queryAllSchedule()
    val liveCourseList = LiveCourseList()
}