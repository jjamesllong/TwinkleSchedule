package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleListViewModel : BaseViewModel() {

    private val _liveScheduleList = MutableLiveData<List<Schedule>>()
    val liveScheduleList: LiveData<List<Schedule>> get() = _liveScheduleList

    override suspend fun onPlace() {
        super.onPlace()
        queryScheduleList()
    }

    fun queryScheduleList() {
        viewModelScope.launch {
            val scheduleList = ScheduleRepository.querySchedules()
            withContext(Dispatchers.Main) {
                _liveScheduleList.value = scheduleList
            }
        }
    }

    fun getScheduleIdByIndex(index: Int): Long? {
        return _liveScheduleList.value?.get(index)?.scheduleId
    }
}