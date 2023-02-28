package com.keycome.twinkleschedule.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_course")
data class CourseEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "master_schedule_id")
    val masterScheduleId: Long,
    val title: String,
)
