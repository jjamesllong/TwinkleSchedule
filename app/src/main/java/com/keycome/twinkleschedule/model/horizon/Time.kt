package com.keycome.twinkleschedule.model.horizon

data class Time(val hour: Int, val minute: Int, val second: Int) {
    fun to24StyleString(withoutSecond: Boolean = true): String {
        val builder = StringBuilder().apply {
            append(if (hour < 10) "0$hour" else hour)
            append(":")
            append(if (minute < 10) "0$minute" else minute)
            if (!withoutSecond) {
                append(":")
                append(if (second < 10) "0$second" else second)
            }
        }
        return builder.toString()
    }
}
