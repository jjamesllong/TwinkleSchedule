package com.keycome.twinkleschedule.preference

import android.content.Context
import android.util.Log
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class Preference private constructor(private val mmkv: MMKV) {

    private val channel: Channel<PrefType> = Channel()

    private val prefLong: MutableMap<String, WeakReference<Flow<Long>>> = mutableMapOf()

    fun ofLong(name: String, default: Long): Flow<Long> {
        return prefLong[name]?.get() ?: flow {
            var oldValue = mmkv.getLong(name, default)
            emit(oldValue)
            for (type in channel) {
                if (type == PrefType.Long) {
                    val newValue = mmkv.getLong(name, default)
                    if (oldValue != newValue) {
                        emit(newValue)
                        oldValue = newValue
                    }
                }
            }
        }.flowOn(Dispatchers.IO).also { flow ->
            prefLong[name] = WeakReference(flow)
        }
    }

    suspend fun writeLong(name: String, value: Long) {
        withContext(Dispatchers.IO) {
            mmkv.encode(name, value)
            channel.send(PrefType.Long)
        }
    }

    companion object {

        private const val TAG = "PREFERENCE"

        fun new(context: Context): Preference {
            val rootDir = MMKV.initialize(context)
            Log.d(TAG, "initialize preference with MMKV, root directory: $rootDir")
            return Preference(MMKV.defaultMMKV())
        }

        const val DEFAULT_LONG = 0L
    }
}
