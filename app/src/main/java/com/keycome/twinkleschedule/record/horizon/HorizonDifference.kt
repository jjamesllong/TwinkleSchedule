package com.keycome.twinkleschedule.record.horizon

object HorizonDifference {

    fun weeklyDiff(startTimeMillis: Long, endTimeMillis: Long): Int {
        val duration = endTimeMillis - startTimeMillis
        return (duration / (1000 * 60 * 60 * 24 * 7)).toInt()
    }
}