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
    var scheduleId: Int,

    var name: String,

    @ColumnInfo(name = "school_begin_date")
    var schoolBeginDate: Date,

    @ColumnInfo(name = "daily_courses")
    var dailyCourses: Int,

    @ColumnInfo(name = "weekly_end_day")
    var weeklyEndDay: Day,

    @ColumnInfo(name = "course_duration")
    var courseDuration: Int,

    @ColumnInfo(name = "time_line")
    var timeLine: Set<TimeLine>
)

data class TimeLine(
    val id: Int,
    val name: String,
    val startDate: Date,
    val timeLineList: List<Time>
)