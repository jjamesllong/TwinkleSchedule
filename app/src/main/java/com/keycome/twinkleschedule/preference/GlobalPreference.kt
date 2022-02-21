package com.keycome.twinkleschedule.preference

import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID_DEFAULT_VALUE
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.launch

object GlobalPreference {
    val displayScheduleId: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>().also {
            App.applicationScope.launch {
                val kv = MMKV.defaultMMKV()
                val id = kv.decodeLong(DISPLAY_SCHEDULE_ID, DISPLAY_SCHEDULE_ID_DEFAULT_VALUE)
                it.postValue(id)
            }
        }
    }
}