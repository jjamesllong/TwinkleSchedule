package com.keycome.twinkleschedule.preference

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LivePreference(private val preference: SharedPreference) {

    private val livePreferenceMap =
        mutableMapOf<String, Pair<MutableStateFlow<*>, MutableSet<LifecycleOwner>>>()

    private var job: Job? = null

    private fun <T> decode(
        lifecycleOwner: LifecycleOwner,
        coroutineScope: CoroutineScope,
        key: String,
        default: T,
        t: () -> T
    ): StateFlow<T> {
        return livePreferenceMap[key]?.let { pair ->
            if (!pair.second.contains(lifecycleOwner))
                pair.second.add(lifecycleOwner)
            @Suppress(names = ["UNCHECKED_CAST"])
            pair.first as StateFlow<T>
        } ?: MutableStateFlow(default).apply {
            coroutineScope.launch {
                val result = withContext(Dispatchers.IO) {
                    t()
                }
                emit(result)
                job = null
            }.also { job = it }
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    job?.cancel()
                    job = null
                    livePreferenceMap[key]?.second?.size?.also { size ->
                        if (size == 1) {
                            livePreferenceMap.remove(key)
                        } else {
                            livePreferenceMap[key]?.second?.remove(lifecycleOwner)
                        }
                    }
                }
            })
            livePreferenceMap[key] = Pair(this, mutableSetOf(lifecycleOwner))
        }.asStateFlow()
    }

    fun decodeLong(
        lifecycleOwner: LifecycleOwner,
        coroutineScope: CoroutineScope,
        key: String,
        default: Long
    ): StateFlow<Long> = decode(lifecycleOwner, coroutineScope, key, default) {
        preference.decodeLong(key, default)
    }

}