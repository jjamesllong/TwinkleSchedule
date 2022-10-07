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
import com.keycome.twinkleschedule.record.interval.Time
import com.keycome.twinkleschedule.record.timetable.Routine
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.record.timetable.Section
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
            launch {
                Pipette.forString.subscribe(KEY_ROUTINE) { routineGson ->
                    val routine = Pipette.gson.fromJson(routineGson, Routine::class.java)
                    val newRoutineList = buildList<Routine> {
                        _liveRoutines.value?.forEach { add(it) }
                        add(routine)
                    }
                    withContext(Dispatchers.Main) {
                        _liveRoutines.value = newRoutineList
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

    private fun ensureScheduleId(): Long {
        return if (scheduleId == 0L) {
            System.currentTimeMillis().also { scheduleId = it }
        } else {
            scheduleId
        }
    }

    fun newRoutine(): Routine {
        val duration = _liveRoutines.value?.let { routineList ->
            return@let if (routineList.isEmpty()) {
                requestDefaultDuration()
            } else {
                if (routineList[0].sectionList.isEmpty()) {
                    requestDefaultDuration()
                } else {
                    Section.fromString(routineList[0].sectionList[0]).duration
                }
            }
        } ?: requestDefaultDuration()
        val routine = _liveRoutines.value?.let { routineList ->
            return@let if (routineList.isEmpty()) {
                requestDefaultRoutine(duration)
            } else {
                routineList[0].copy(routineId = System.currentTimeMillis())
            }
        } ?: requestDefaultRoutine(duration)
        return routine
    }

    fun deleteRoutine(routineId: Long) {
        val routines = _liveRoutines.value?.filter { it.routineId != routineId }
        routines?.also { _liveRoutines.value = it }
    }

    private fun requestDefaultDuration(): Int {
        return 45 * 60
    }

    private fun requestDefaultRoutine(duration: Int): Routine {
        val sectionList = listOf(
            "08:00",
            "08:55",
            "10:00",
            "10:55",
            "14:00",
            "14:55",
            "15:50",
            "16:45",
            "17:40",
            "19:00"
        ).map { Time.from(it) }.mapIndexed { index, time ->
            val order = index + 1
            val to = time + duration
            Section(order, time, to)
        }.map { it.toString() }
        return Routine(
            routineId = System.currentTimeMillis(),
            masterId = ensureScheduleId(),
            routineName = "default",
            startDate = Date.today().toString(),
            sectionList = sectionList
        )
    }

    private fun requestSchedule(): Schedule? {
        val scheduleId = ensureScheduleId()
        val scheduleName = _liveScheduleName.value?.let {
            it.ifBlank { return null }
        } ?: return null
        val startDate = _liveStartDate.value?.toString() ?: return null
        val endSection = _liveEndSection.value?.let {
            if (it > 20) {
                return null
            } else {
                return@let it
            }
        } ?: return null
        val endDay = _liveEndDay.value?.let {
            if (it !in 1..7) {
                return null
            } else {
                return@let it
            }
        } ?: return null
        val endWeek = _liveEndWeek.value?.let {
            if (it !in 1..30) {
                return null
            } else {
                return@let it
            }
        } ?: return null
        return Schedule(scheduleId, scheduleName, startDate, endSection, endDay, endWeek)
    }

    private fun requestRoutines(): List<Routine> {
        return _liveRoutines.value?.let { routineList ->
            val endSection = _liveEndSection.value ?: -1
            routineList.forEach {
                if (it.sectionList.size != endSection) {
                    return emptyList()
                }
            }
            return@let routineList
        } ?: emptyList()
    }

    suspend fun writeSchedule(display: Boolean, requestModification: Boolean): Boolean {
        val schedule = requestSchedule() ?: return false
        val routines = requestRoutines()
        if (routines.isEmpty()) {
            return false
        }
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
        return true
    }

}