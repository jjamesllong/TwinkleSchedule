package com.keycome.twinkleschedule.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_schedule")
data class ScheduleEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    @ColumnInfo(name = "start_date")
    val startDate: String,
    @ColumnInfo(name = "sections_per_day")
    val sectionsPerDay: Int,
    @ColumnInfo(name = "days_per_week")
    val daysPerWeek: Int,
    @ColumnInfo(name = "weeks_per_term")
    val weeksPerTerm: Int,
)
