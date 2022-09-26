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
import com.keycome.twinkleschedule.record.timetable.Section
import com.keycome.twinkleschedule.util.const.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRoutineViewModel : BaseViewModel() {

    var sectionSize = 0

    var routineId = 0L

    var masterId = 0L

    private val _liveRoutineName = MutableLiveData<String>()
    val liveRoutineName: LiveData<String> = _liveRoutineName.asLiveData()

    private val _liveStartDate = MutableLiveData<Date>()
    val liveStartDate: LiveData<Date> = _liveStartDate.asLiveData()

    private val _liveSectionDuration = MutableLiveData<Int>()
    val liveSectionDuration: LiveData<Int> = _liveSectionDuration.asLiveData()

    private val _liveSectionList = MutableLiveData<List<Section>>()
    val liveSectionList: LiveData<List<Section>> = _liveSectionList.asLiveData()

    override suspend fun onPlace() {
        super.onPlace()
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                Pipette.forString.subscribe(KEY_ROUTINE_NAME) {
                    withContext(Dispatchers.Main) {
                        _liveRoutineName.value = it
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(KEY_ROUTINE_START_DATE) {
                    withContext(Dispatchers.Main) {
                        _liveStartDate.value = Date.fromString(it)
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(KEY_ROUTINE_SECTION_DURATION) {
                    withContext(Dispatchers.Main) {
                        refreshSectionDuration(it)
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(KEY_ROUTINE_SECTION_START_TIME) {
                    val s = it.split(delimiters = arrayOf("@"), ignoreCase = false, limit = 0)
                    val time = Time.from(s[0])
                    val index = s[1].toInt()
                    withContext(Dispatchers.Main) {
                        if (index < 0) {
                            insertSectionByTime(time)
                        } else {
                            updateSectionByIndex(index, time)
                        }
                    }
                }
            }
        }
    }

    fun refreshRoutineName(name: String) {
        _liveRoutineName.value = name
    }

    fun refreshStartDate(date: Date) {
        _liveStartDate.value = date
    }

    fun refreshSectionDuration(duration: Int) {
        val old = _liveSectionDuration.value ?: 0
        if (duration == 0 || duration == old)
            return
        val sectionList = _liveSectionList.value?.mapIndexed { i, s ->
            val order = i + 1
            val from = s.from
            val to = s.from + (duration * 60)
            Section(order, from, to)
        }
        sectionList?.also { _liveSectionList.value = it }
        _liveSectionDuration.value = duration
    }

    fun refreshSectionListByString(list: List<String>) {
        list.map { Section.fromString(it) }.also {
            _liveSectionList.value = it
            inferDurationBySectionList(it)
        }
    }

    fun refreshSectionListByTime(list: List<Time>) {
        val duration = _liveSectionDuration.value ?: 0
        _liveSectionList.value = list.mapIndexed { i, t ->
            val order = i + 1
            val to = t + (duration * 60)
            Section(order, t, to)
        }.also {
            inferDurationBySectionList(it)
        }
    }

    fun refreshSectionListBySection(list: List<Section>) {
        _liveSectionList.value = list
        inferDurationBySectionList(list)
    }

    private fun inferDurationBySectionList(list: List<Section>) {
        val duration = if (list.isEmpty()) {
            0
        } else {
            (list[0].duration) / 60
        }
        if (duration == _liveSectionDuration.value) {
            return
        }
        _liveSectionDuration.value = duration
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
        _liveSectionList.value?.also { sectionList ->
            val timeList = sectionList.map { it.from }.toMutableList()
            deleteTime(timeList, index)
            refreshSectionListByTime(timeList)
        }
    }

    fun insertSectionByTime(time: Time) {
        _liveSectionList.value?.also { sectionList ->
            val timeList = sectionList.map { it.from }.toMutableList()
            insertTime(timeList, time)
            refreshSectionListByTime(timeList)
        }
    }

    fun updateSectionByIndex(oldTimeIndex: Int, newTime: Time) {
        _liveSectionList.value?.also { sectionList ->
            val timeList = sectionList.map { it.from }.toMutableList()
            updateTime(timeList, oldTimeIndex, newTime)
            refreshSectionListByTime(timeList)
        }
    }

    private fun checkRoutine(): Boolean {
        if (_liveRoutineName.value.isNullOrBlank())
            return false
        if (_liveStartDate.value == null)
            return false
        if ((_liveSectionDuration.value ?: 0) == 0)
            return false
        _liveSectionList.value?.also {
            if (it.isEmpty() || it.size != sectionSize) {
                return false
            }
            for (i in it.indices) {
                if (i != 0) {
                    val p = i - 1
                    if (it[i].from.toSeconds() <= it[p].to.toSeconds()) {
                        return false
                    }
                }
            }
        } ?: return false
        return true
    }

    suspend fun submitRoutine(): Boolean = withContext(Dispatchers.IO) {
        val condition = checkRoutine()
        return@withContext if (condition) {
            val ri = routineId
            val mi = masterId
            val rn = liveRoutineName.value!!
            val sd = liveStartDate.value!!.toString()
            val sl = liveSectionList.value!!.map { it.toString() }
            val routine = Routine(ri, mi, rn, sd, sl)
            Pipette.forString.distribute(KEY_ROUTINE) {
                Pipette.gson.toJson(routine)
            }
            true
        } else {
            false
        }
    }

}