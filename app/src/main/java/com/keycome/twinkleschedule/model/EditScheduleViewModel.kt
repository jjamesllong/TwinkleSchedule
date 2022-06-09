package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.subscribe
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.DailyRoutine
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.repository.DailyRoutineRepository
import com.keycome.twinkleschedule.repository.ScheduleRepository
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditScheduleViewModel : BaseViewModel() {

    private var isModify = false

    var scheduleId = System.currentTimeMillis()
        private set

    private val _liveScheduleName by sharePostVariable(sharedScheduleName) {
        MutableLiveData("新课表")
    }
    val liveScheduleName: LiveData<String> get() = _liveScheduleName

    private val _liveSchoolOpeningDate by sharePostVariable(sharedSchoolOpeningDate) {
        MutableLiveData(Date.currentDate())
    }
    val liveSchoolOpeningDate: LiveData<Date> get() = _liveSchoolOpeningDate

    private val _liveDailyCourses by sharePostVariable(sharedDailyCourses) {
        MutableLiveData(10)
    }
    val liveDailyCourses: LiveData<Int> get() = _liveDailyCourses

    private val _liveEndDay by sharePostVariable(sharedEndDay) {
        MutableLiveData(Day.Friday)
    }
    val liveEndDay: LiveData<Day> get() = _liveEndDay

    private val _liveWeeks = MutableLiveData(20)
    val liveWeeks: LiveData<Int> get() = _liveWeeks

    private val _liveDailyRoutines = MutableLiveData(
        listOf(
            DailyRoutine(
                dailyRoutineId = System.currentTimeMillis(),
                name = "默认作息",
                parentScheduleId = scheduleId,
                startDate = liveSchoolOpeningDate.value ?: Date.currentDate(),
                routines = TestData.getSectionList()
            )
        )
    )
    val liveDailyRoutines: LiveData<List<DailyRoutine>> get() = _liveDailyRoutines

    override suspend fun onPlace() {
        super.onPlace()
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                Pipette.forInt.subscribe(sharedWeeks) {
                    withContext(Dispatchers.Main) {
                        _liveWeeks.value = it
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(sharedDailyRoutines) { jsonString ->
                    val dailyRoutine = Pipette.gson.fromJson(jsonString, DailyRoutine::class.java)
                    val list = buildList<DailyRoutine> {
                        _liveDailyRoutines.value?.forEach {
                            if (it.dailyRoutineId != dailyRoutine.dailyRoutineId) {
                                add(it)
                            }
                        }
                        add(dailyRoutine)
                    }
                    withContext(Dispatchers.Main) {
                        _liveDailyRoutines.value = list
                    }
                }
            }
        }
    }

    override fun onRemove() {
        super.onRemove()
        release(
            sharedScheduleName,
            sharedSchoolOpeningDate,
            sharedDailyCourses,
            sharedEndDay,
        )
    }

    fun requestModifiedDataById(parentScheduleId: Long) {
        isModify = true
        viewModelScope.launch {
            launch {
                val schedule = ScheduleRepository.querySchedule(parentScheduleId)
                schedule?.let {
                    scheduleId = it.scheduleId
                    _liveScheduleName.value = it.name
                    _liveSchoolOpeningDate.value = it.schoolOpeningDate
                    _liveDailyCourses.value = it.endSection
                    _liveEndDay.value = Day.fromNumber(it.endDay)
                    _liveWeeks.value = it.endWeek
                }
            }
            launch {
                val dailyRoutines = DailyRoutineRepository.queryDailyRoutines(parentScheduleId)
                _liveDailyRoutines.value = dailyRoutines
            }
        }
    }

    fun refreshSchoolOpeningDate(date: Date) {
        _liveSchoolOpeningDate.value = date
    }

    fun checkScheduleRight(): Boolean {
        if (_liveScheduleName.value.isNullOrBlank())
            return false
        if ((_liveDailyRoutines.value?.size ?: 0) == 0) {
            return false
        }
        _liveDailyRoutines.value?.also {
            it.indices - 1
            val indices = 0..(it.size - 2)
            val last = it.lastIndex
            for (i in indices) {
                for (j in (i + 1)..last) {
                    if (it[i].startDate == it[j].startDate)
                        return false
                }
            }
        }
        return true
    }

    suspend fun insertSchedule(display: Boolean) {
        val schedule = Schedule(
            scheduleId = scheduleId,
            name = _liveScheduleName.value!!,
            schoolOpeningDate = _liveSchoolOpeningDate.value!!,
            endSection = _liveDailyCourses.value!!,
            endDay = _liveEndDay.value!!.toNumber(),
            endWeek = _liveWeeks.value!!
        )
        val dailyRoutine = _liveDailyRoutines.value!!
        withContext(NonCancellable) {
            if (isModify) {
                ScheduleRepository.updateSchedule(schedule)
                dailyRoutine.forEach { DailyRoutineRepository.updateDailyRoutine(it) }
            } else {
                ScheduleRepository.insertSchedule(schedule)
                dailyRoutine.forEach { DailyRoutineRepository.insertDailyRoutine(it) }
            }
            if (display) {
                MMKV.defaultMMKV().encode(DISPLAY_SCHEDULE_ID, scheduleId)
                withContext(Dispatchers.Main) {
                    GlobalPreference.displayScheduleId.value = schedule.scheduleId
                }
            }
        }
    }

    fun requestDailyRoutine(): DailyRoutine {
        return if (_liveDailyRoutines.value.isNullOrEmpty()) {
            DailyRoutine(
                dailyRoutineId = System.currentTimeMillis(),
                name = "默认作息",
                parentScheduleId = scheduleId,
                startDate = liveSchoolOpeningDate.value ?: Date.currentDate(),
                routines = TestData.getSectionList()
            )
        } else {
            _liveDailyRoutines.value!![0].copy(
                dailyRoutineId = System.currentTimeMillis(),
                name = "新作息"
            )
        }
    }

    fun deleteDailyRoutine(id: Long): Boolean {
        return if ((_liveDailyRoutines.value?.size ?: 0) <= 1) false else {
            val list = _liveDailyRoutines.value?.filterTo(ArrayList()) {
                it.dailyRoutineId != id
            }
            list?.also { _liveDailyRoutines.value = it }
            true
        }
    }

    companion object {

        const val sharedScheduleName = "shared_schedule_name"
        const val sharedSchoolOpeningDate = "shared_school_Opening_date"
        const val sharedDailyCourses = "shared_daily_coursed"
        const val sharedEndDay = "shared_end_day"
        const val sharedWeeks = "shared_weeks"
        const val sharedDailyRoutines = "shared_daily_routines"
    }
}