package com.keycome.twinkleschedule.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_routine")
data class RoutineEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "master_schedule_id")
    val masterScheduleId: Long,
    val name: String,
    @ColumnInfo(name = "start_date")
    val startDate: String,
)
