package com.keycome.twinkleschedule

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("StaticFieldLeak")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        nullableContext = applicationContext
        MMKV.initialize(this)
    }

    companion object {

        private var nullableContext: Context? = null
        val context get() = nullableContext!!

        val applicationScope: CoroutineScope =
            CoroutineScope(SupervisorJob() + Dispatchers.Default)

    }
}