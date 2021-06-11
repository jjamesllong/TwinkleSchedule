package com.keycome.twinkleschedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_entity")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    val scheduleId: Int,

    val name: String,

    @ColumnInfo(name = "school_begin_date")
    val schoolBeginDate: Date,

    val courses: Int,

    val duration: Int,

    @ColumnInfo(name = "time_line")
    val timeLine: Map<String, List<String>>
)
//{
//    constructor() : this(
//        scheduleId = 0,
//        name = "",
//        schoolBeginDate = Date(1970, 1, 1),
//        courses = 0,
//        duration = 0,
//        timeLine = mapOf("" to listOf<String>(""))
//    )
//}

data class Date(val year: Int, val month: Int, val dayOfMonth: Int) {
    init {
        if (year !in 1970..9999)
            throw IllegalArgumentException("the format of parameter year is incorrect")
        if (month !in 1..12)
            throw IllegalArgumentException("parameter month should between [1, 12]")
        if (dayOfMonth !in 1..31)
            throw IllegalArgumentException("parameter dayOfYear should between [1, 31]")
    }
}