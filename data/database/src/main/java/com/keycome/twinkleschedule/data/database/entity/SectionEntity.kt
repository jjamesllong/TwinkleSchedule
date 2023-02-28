package com.keycome.twinkleschedule.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_section")
data class SectionEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "master_course_id")
    val masterCourseId: Long,
    val teacher: String,
    val classroom: String,
    @ColumnInfo(name = "start_of_day")
    val startOfDay: Int,
    @ColumnInfo(name = "end_of_day")
    val endOfDay: Int,
    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: Int,
    @ColumnInfo(name = "week_of_term")
    val weekOfTerm: Int,
)
