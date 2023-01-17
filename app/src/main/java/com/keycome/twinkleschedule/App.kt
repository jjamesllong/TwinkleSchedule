package com.keycome.twinkleschedule

import android.app.Application
import android.content.Context
import com.keycome.twinkleschedule.preference.Preference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("StaticFieldLeak")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        _context = applicationContext
        _preference = Preference.new(applicationContext)
    }

    companion object {

        private var _context: Context? = null
        val context get() = _context!!

        private var _preference: Preference? = null
        val preference get() = _preference!!

        val appScope: CoroutineScope =
            CoroutineScope(SupervisorJob() + Dispatchers.Default)

    }
}