package com.keycome.twinkleschedule.presentation.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.repository.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigurationViewModel : ViewModel() {

    val liveScheduleList: LiveData<List<ScheduleEntity>> by lazy { Repository.queryAllSchedule() }

    fun insertSchedule(schedule: ScheduleEntity) {
        GlobalScope.launch {
            Repository.insertSchedule(schedule)
        }
    }
}