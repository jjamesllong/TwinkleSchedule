package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.record.sketch.Schedule
import com.keycome.twinkleschedule.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleDetailsViewModel : BaseViewModel() {

    private val _liveSchedule = MutableLiveData<Schedule>()
    val liveSchedule: LiveData<Schedule> get() = _liveSchedule

    fun querySchedule(id: Long) {
        viewModelScope.launch {
            val schedule = ScheduleRepository.querySchedule(id)
            _liveSchedule.value = schedule
        }
    }

    suspend fun deleteSchedule(id: Long) {
        ScheduleRepository.deleteSchedule(id)
    }

    fun queryScheduleId(): Long {
        return _liveSchedule.value?.scheduleId ?: 0L
    }
}