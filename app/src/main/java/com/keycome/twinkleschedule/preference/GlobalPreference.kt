package com.keycome.twinkleschedule.preference

import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID_DEFAULT_VALUE
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object GlobalPreference {
    val displayScheduleId: MutableLiveData<Long> by lazy {
        MutableLiveData(DISPLAY_SCHEDULE_ID_DEFAULT_VALUE).also {
            App.applicationScope.launch {
                val kv = MMKV.defaultMMKV()
                val id = kv.decodeLong(DISPLAY_SCHEDULE_ID, DISPLAY_SCHEDULE_ID_DEFAULT_VALUE)
                it.postValue(id)
            }
        }
    }

    private val mutableFavorLong = create<Long>()

    val favorLong: SharedFlow<Favor<Long>> = mutableFavorLong.asSharedFlow()

    suspend fun postLong(favor: Favor<Long>) {
        mutableFavorLong.emit(favor)
    }

    private fun <T> create(): MutableSharedFlow<Favor<T>> {
        return MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 0,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }
}