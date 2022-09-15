package com.keycome.twinkleschedule.extension.days

import android.content.Context
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.record.interval.Day

context(Context)
fun Day.toLocalWord(): String {
    return when (this) {
        Day.Monday -> getString(R.string.monday)
        Day.Tuesday -> getString(R.string.tuesday)
        Day.Wednesday -> getString(R.string.wednesday)
        Day.Thursday -> getString(R.string.thursday)
        Day.Friday -> getString(R.string.friday)
        Day.Saturday -> getString(R.string.saturday)
        Day.Sunday -> getString(R.string.sunday)
    }
}