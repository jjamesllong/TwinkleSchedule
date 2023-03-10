package com.keycome.twinkleschedule.record.timetable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.interval.Interval

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "schedule_id")
    val scheduleId: Long,

    @ColumnInfo(name = "schedule_name")
    val scheduleName: String,

    @ColumnInfo(name = "start_date")
    val startDate: String,

    @ColumnInfo(name = "end_section")
    val endSection: Int,

    @ColumnInfo(name = "end_day")
    val endDay: Int,

    @ColumnInfo(name = "end_week")
    val endWeek: Int
) {
    fun inferWeekNow(): Int {
        val date = Date.fromString(startDate)
        val startTimeMillis = date.toMilliSeconds()
        val currentTimeMillis = System.currentTimeMillis()
        return when {
            startTimeMillis > currentTimeMillis -> -1
            else -> {
                val weekNow = Interval.weeklyInterval(startTimeMillis, currentTimeMillis) + 1
                if (weekNow > endWeek) 0 else weekNow
            }
        }
    }
}