package com.keycome.twinkleschedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.subscribe
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.interval.Time
import com.keycome.twinkleschedule.record.timetable.Section
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRoutineViewModel : BaseViewModel() {

    var dailyRoutineId = System.currentTimeMillis()

    var parentScheduleId = 0L

    private val _liveEditName = MutableLiveData<String>()
    val liveEditName: LiveData<String> get() = _liveEditName

    private val _liveEditDate = MutableLiveData<Date>()
    val liveEditDate: LiveData<Date> get() = _liveEditDate

    private val _liveEditDuration = MutableLiveData(0)
    val liveEditDuration: LiveData<Int> get() = _liveEditDuration

    var dailyCourses = 0

    private val _liveEditSectionList = MutableLiveData<List<Section>>()
    val liveEditSectionList: LiveData<List<Section>> get() = _liveEditSectionList

    override suspend fun onPlace() {
        super.onPlace()
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                Pipette.forInt.subscribe(courseDuration) {
                    withContext(Dispatchers.Main) {
                        refreshDuration(it)
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(dailyRoutineName) {
                    withContext(Dispatchers.Main) {
                        refreshName(it)
                    }
                }
            }
        }
    }

    fun refreshName(name: String) {
        _liveEditName.value = name
    }

    fun refreshDate(date: Date) {
        _liveEditDate.value = date
    }

    fun refreshDuration(duration: Int) {
        val old = _liveEditDuration.value ?: 0
        if (duration == 0 || duration == old)
            return
        val sectionList = _liveEditSectionList.value?.map {
            Section(it.from, it.from + duration * 60)
        }
        sectionList?.also { _liveEditSectionList.value = it }
        _liveEditDuration.value = duration
    }

    fun refreshSectionList(list: MutableList<Time>) {
        val duration = _liveEditDuration.value ?: 0
        _liveEditSectionList.value = list.map { Section(it, it + duration * 60) }
    }

    fun refreshRoutines(list: List<Section>) {
        _liveEditSectionList.value = list
    }

    private fun deleteTime(list: MutableList<Time>, index: Int) {
        list.removeAt(index)
    }

    private fun insertTime(list: MutableList<Time>, time: Time) {
        val seconds = time.toSeconds()
        var index = -1
        for (i in list.indices) {
            val indexSeconds = list[i].toSeconds()
            if (seconds == indexSeconds) return
            if (seconds < indexSeconds) {
                index = i
                break
            }
        }
        if (index == -1) index = list.size
        list.add(index, time)
    }

    private fun updateTime(list: MutableList<Time>, oldTimeIndex: Int, newTime: Time) {
        if (list[oldTimeIndex].toSeconds() == newTime.toSeconds()) return
        deleteTime(list, oldTimeIndex)
        insertTime(list, newTime)
    }

    fun deleteSectionByIndex(index: Int) {
        _liveEditSectionList.value?.let { sectionList ->
            val timeList = sectionList.map { it.from }.toMutableList()
            deleteTime(timeList, index)
            refreshSectionList(timeList)
        }
    }

    fun insertSectionByTime(time: Time) {
        _liveEditSectionList.value?.let { sectionList ->
            val timeList = sectionList.map { it.from }.toMutableList()
            insertTime(timeList, time)
            refreshSectionList(timeList)
        }
    }

    fun updateSectionByIndex(oldTimeIndex: Int, newTime: Time) {
        _liveEditSectionList.value?.let { sectionList ->
            val timeList = sectionList.map { it.from }.toMutableList()
            updateTime(timeList, oldTimeIndex, newTime)
            refreshSectionList(timeList)
        }
    }

    private fun checkDailyRoutine(): Boolean {
        if (_liveEditName.value.isNullOrBlank())
            return false
        if (_liveEditDate.value == null)
            return false
        if ((_liveEditDuration.value ?: 0) == 0)
            return false
        _liveEditSectionList.value?.also {
            if (it.isNotEmpty()) {
                for (i in it.indices) {
                    if (i != 0) {
                        val p = i - 1
                        if (it[i].from.toSeconds() <= it[p].to.toSeconds()) {
                            return false
                        }
                    }
                }
            } else return false
        } ?: return false
        return true
    }

    suspend fun submitDailyRoutine(): Boolean = withContext(Dispatchers.IO) {
//        val condition = checkDailyRoutine()
//        return@withContext if (condition) {
//            val dailyRoutine = Routine(
//                dailyRoutineId = dailyRoutineId,
//                parentScheduleId = parentScheduleId,
//                name = liveEditName.value!!,
//                startDate = liveEditDate.value!!,
//                routines = liveEditSectionList.value!!
//            )
//            val jsonString = Pipette.gson.toJson(dailyRoutine)
//            Pipette.forString.emit(
//                Drop(
//                    EditScheduleViewModel.sharedDailyRoutines,
//                    jsonString
//                )
//            )
//            true
//        } else false
        false
    }

    companion object {

        const val dailyRoutineName = "daily_routine_name"
        const val courseDuration = "course_duration"
    }
}