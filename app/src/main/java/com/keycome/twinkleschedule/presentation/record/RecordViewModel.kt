package com.keycome.twinkleschedule.presentation.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.repository.Repository

class RecordViewModel : ViewModel() {
    val liveScheduleList: LiveData<List<ScheduleEntity>> = Repository.queryAllSchedule()
}