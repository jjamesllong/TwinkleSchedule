package com.keycome.twinkleschedule.extension.days

import android.content.Context
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.record.interval.Day

fun Day.toLocalWord(context: Context): String {
    return when (this) {
        Day.Monday -> context.getString(R.string.monday)
        Day.Tuesday -> context.getString(R.string.tuesday)
        Day.Wednesday -> context.getString(R.string.wednesday)
        Day.Thursday -> context.getString(R.string.thursday)
        Day.Friday -> context.getString(R.string.friday)
        Day.Saturday -> context.getString(R.string.saturday)
        Day.Sunday -> context.getString(R.string.sunday)
    }
}