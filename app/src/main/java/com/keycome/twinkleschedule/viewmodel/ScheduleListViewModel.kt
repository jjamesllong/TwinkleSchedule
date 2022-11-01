package com.keycome.twinkleschedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.extension.asLiveData
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.repository.ScheduleRepository
import com.keycome.twinkleschedule.util.const.KEY_DISPLAY_SCHEDULE_ID
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleListViewModel : BaseViewModel() {

    private val _liveUsingScheduleId = MutableLiveData<Long>()
    val liveUsingScheduleId: LiveData<Long> = _liveUsingScheduleId.asLiveData()

    private val _liveScheduleList = MutableLiveData<List<Schedule>>()
    val liveScheduleList: LiveData<List<Schedule>> = _liveScheduleList.asLiveData()

    override suspend fun onPlace() {
        super.onPlace()
        coroutineScope {
            launch(Dispatchers.IO) {
                val id = MMKV.defaultMMKV().decodeLong(KEY_DISPLAY_SCHEDULE_ID)
                withContext(Dispatchers.Main) {
                    _liveUsingScheduleId.value = id
                }
            }
            ScheduleRepository.querySchedulesFlow().collect {
                _liveScheduleList.value = it
            }
        }
    }

    fun getScheduleByIndex(index: Int): Schedule? {
        return _liveScheduleList.value?.get(index)
    }
}