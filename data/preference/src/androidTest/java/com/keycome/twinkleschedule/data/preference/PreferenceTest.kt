package com.keycome.twinkleschedule.data.preference

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferenceTest {

    lateinit var preference: Preference

    private val tag = "PreferenceTest"

    @Before
    @Throws(Exception::class)
    fun loadPreference() {
        val context = InstrumentationRegistry.getInstrumentation().context
        preference = Preference.getBy(context)
    }

    @Test
    @Throws(Exception::class)
    fun testPreference() {
        val name1 = "firstEncoding"
        val name2 = "secondEncoding"
        preference.writeString(name1, "the value of firstEncoding")
        preference.subscribeString(name1) {
            Log.d(tag, "subscriber 1: name: $name1 value: $it")
        }
        preference.subscribeString(name1) {
            Log.d(tag, "subscriber 2: name: $name1 value: $it")
        }
        preference.subscribeString(name2) {
            Log.d(tag, "subscriber 3: name: $name2 value: $it")
        }
        preference.subscribeString(name2) {
            Log.d(tag, "subscriber 4: name: $name2 value: $it")
        }
        preference.writeString(name2, "the value of secondEncoding")
    }
}