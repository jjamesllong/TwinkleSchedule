package com.keycome.twinkleschedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keycome.twinkleschedule.model.Date
import com.keycome.twinkleschedule.model.Day
import com.keycome.twinkleschedule.model.Time

@Entity(tableName = "schedule_entity")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    val scheduleId: Int,

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
    val timeLine: Map<String, TimeLine>
)

data class TimeLine(val startDate: Date, val timeLineList: List<Time>)