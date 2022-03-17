package com.keycome.twinkleschedule

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("StaticFieldLeak")
class App : Application() {
    companion object {

        private var myContext: Context? = null
        val context get() = myContext!!

        val applicationScope: CoroutineScope =
            CoroutineScope(SupervisorJob() + Dispatchers.Default)

    }

    override fun onCreate() {
        super.onCreate()
        myContext = applicationContext
        MMKV.initialize(this)
    }
}