package com.keycome.twinkleschedule

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID_DEFAULT_VALUE
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Suppress("StaticFieldLeak")
class App : Application() {
    companion object {
        private var myContext: Context? = null
        val context get() = myContext!!

        val applicationScope: CoroutineScope =
            CoroutineScope(SupervisorJob() + Dispatchers.Default)

        val displayScheduleId: MutableLiveData<Long> by lazy {
            MutableLiveData<Long>().also {
                applicationScope.launch {
                    val kv = MMKV.defaultMMKV()
                    val id = kv.decodeLong(DISPLAY_SCHEDULE_ID, DISPLAY_SCHEDULE_ID_DEFAULT_VALUE)
                    it.postValue(id)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        myContext = applicationContext
        MMKV.initialize(this)
    }
}