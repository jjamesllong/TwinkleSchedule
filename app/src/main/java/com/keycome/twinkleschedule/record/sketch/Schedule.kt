package com.keycome.twinkleschedule.record.sketch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keycome.twinkleschedule.record.span.Date
import com.keycome.twinkleschedule.record.span.Day
import com.keycome.twinkleschedule.record.span.Time

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    val scheduleId: Long,

    val name: String,

    @ColumnInfo(name = "school_begin_date")
    val schoolBeginDate: Date,

    @ColumnInfo(name = "daily_courses")
    val dailyCourses: Int,

    @ColumnInfo(name = "weekly_end_day")
    val weeklyEndDay: Day,

    @ColumnInfo(name = "course_duration")
    val courseDuration: Int,

    @ColumnInfo(name = "time_line")
    val timeLine: Set<TimeLine>
)

data class TimeLine(
    val id: Int,
    val name: String,
    val startDate: Date,
    val lineList: List<Time>
)