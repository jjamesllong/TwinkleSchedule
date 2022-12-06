package com.keycome.twinkleschedule.preference

import android.content.Context
import android.util.Log
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.withContext

class Preference private constructor(private val mmkv: MMKV) {

    private val longChannel: MutableMap<String, MutableSet<Channel<Long>>> = mutableMapOf()

    fun decodeLong(name: String, default: Long): Flow<Long> {
        val channel = Channel<Long>()
        return flow {
            var oldValue = mmkv.getLong(name, default)
            emit(oldValue)
            val set = longChannel[name] ?: mutableSetOf()
            set.add(channel)
            longChannel[name] = set
            for (newValue in channel) {
                if (oldValue != newValue) {
                    emit(newValue)
                    oldValue = newValue
                }
            }
        }.flowOn(Dispatchers.IO).onCompletion {
            longChannel[name]?.remove(channel)
            if ((longChannel[name]?.size ?: 0) == 0) {
                longChannel.remove(name)
            }
        }
    }

    suspend fun encodeLong(name: String, value: Long) {
        withContext(Dispatchers.IO) {
            mmkv.encode(name, value)
            longChannel[name]?.forEach { channel ->
                channel.send(value)
            }
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
