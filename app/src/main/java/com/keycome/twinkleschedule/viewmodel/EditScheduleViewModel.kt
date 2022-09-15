package com.keycome.twinkleschedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.delivery.Pipette.subscribe
import com.keycome.twinkleschedule.extension.asLiveData
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.timetable.Routine
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.repository.RoutineRepository
import com.keycome.twinkleschedule.repository.ScheduleRepository
import com.keycome.twinkleschedule.util.const.*
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditScheduleViewModel : BaseViewModel() {

    var scheduleId = 0L
        private set

    private val _liveScheduleName = MutableLiveData<String>()
    val liveScheduleName: LiveData<String> = _liveScheduleName.asLiveData()

    private val _liveStartDate = MutableLiveData<Date>()
    val liveStartDate: LiveData<Date> = _liveStartDate.asLiveData()

    private val _liveEndSection = MutableLiveData<Int>()
    val liveEndSection = _liveEndSection.asLiveData()

    private val _liveEndDay = MutableLiveData<Int>()
    val liveEndDay: LiveData<Int> = _liveEndDay.asLiveData()

    private val _liveEndWeek = MutableLiveData<Int>()
    val liveEndWeek: LiveData<Int> = _liveEndWeek.asLiveData()

    private val _liveRoutines = MutableLiveData<List<Routine>>()
    val liveRoutines: LiveData<List<Routine>> = _liveRoutines.asLiveData()

    override suspend fun onPlace() {
        super.onPlace()
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                Pipette.forString.subscribe(KEY_SCHEDULE_NAME) {
                    withContext(Dispatchers.Main) {
                        _liveScheduleName.value = it
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(KEY_SCHEDULE_START_DATE) {
                    withContext(Dispatchers.Main) {
                        _liveStartDate.value = Date.fromString(it)
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(KEY_SCHEDULE_END_SECTION) {
                    withContext(Dispatchers.Main) {
                        _liveEndSection.value = it
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(KEY_SCHEDULE_END_DAY) {
                    withContext(Dispatchers.Main) {
                        _liveEndDay.value = it
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(KEY_SCHEDULE_END_WEEK) {
                    withContext(Dispatchers.Main) {
                        _liveEndWeek.value = it
                    }
                }
            }
        }
    }

    fun loadScheduleFromDatabase(id: Long) {
        viewModelScope.launch {
            launch {
                val schedule = ScheduleRepository.querySchedule(id)
                schedule?.let {
                    scheduleId = it.scheduleId
                    _liveScheduleName.value = it.scheduleName
                    _liveStartDate.value = Date.fromString(it.startDate)
                    _liveEndSection.value = it.endSection
                    _liveEndDay.value = it.endDay
                    _liveEndWeek.value = it.endWeek
                }
            }
            launch {
                val routines = RoutineRepository.queryRoutinesOfSchedule(id)
                _liveRoutines.value = routines
            }
        }
    }

    fun setStartDate(date: Date) {
        _liveStartDate.value = date
    }

    private fun requestSchedule(): Schedule {
        TODO()
    }

    private fun requestRoutines(): List<Routine> {
        TODO()
    }

    suspend fun writeSchedule(display: Boolean, requestModification: Boolean) {
        val schedule = requestSchedule()
        val routines = requestRoutines()
        withContext(NonCancellable) {
            if (requestModification) {
                ScheduleRepository.updateSchedule(schedule)
                routines.forEach { RoutineRepository.updateRoutine(it) }
            } else {
                ScheduleRepository.insertSchedule(schedule)
                routines.forEach { RoutineRepository.insertRoutine(it) }
            }
            if (display) {
                MMKV.defaultMMKV().encode(KEY_DISPLAY_SCHEDULE_ID, scheduleId)
                Pipette.forEvent.distribute { EVENT_WRITE_DISPLAY_SCHEDULE_ID }
            }
        }
    }

}