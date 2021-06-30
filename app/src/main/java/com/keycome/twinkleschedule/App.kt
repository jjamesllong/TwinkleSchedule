package com.keycome.twinkleschedule

import android.app.Application
import android.content.Context

@Suppress("StaticFieldLeak")
class App : Application() {
    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}