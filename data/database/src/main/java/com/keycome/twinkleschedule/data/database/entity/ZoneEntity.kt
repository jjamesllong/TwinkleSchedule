package com.keycome.twinkleschedule.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_zone")
data class ZoneEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "master_routine_id")
    val masterRoutineId: Long,
    val position: Int,
    @ColumnInfo(name = "start_second_in_day")
    val startSecondInDay: Int,
    @ColumnInfo(name = "end_second_in_day")
    val endSecondInDay: Int,
)
