package com.keycome.twinkleschedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.extension.asLiveData
import com.keycome.twinkleschedule.record.timetable.Routine
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.repository.RoutineRepository
import com.keycome.twinkleschedule.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleDetailsViewModel : BaseViewModel() {

    private val _liveSchedule = MutableLiveData<Schedule>()
    val liveSchedule: LiveData<Schedule> = _liveSchedule.asLiveData()

    private val _liveRoutines = MutableLiveData<List<Routine>>()
    val liveRoutines: LiveData<List<Routine>> = _liveRoutines.asLiveData()

    fun querySchedule(id: Long) {
        viewModelScope.launch {
            launch {
                val schedule = ScheduleRepository.querySchedule(id)
                schedule?.also { _liveSchedule.value = it }
            }
            launch {
                val routines = RoutineRepository.queryRoutinesOfSchedule(id)
                _liveRoutines.value = routines
            }
        }
    }

    suspend fun deleteSchedule(id: Long) {
        ScheduleRepository.deleteSchedule(id)
    }

    fun queryScheduleId(): Long {
        return _liveSchedule.value?.scheduleId ?: 0L
    }
}