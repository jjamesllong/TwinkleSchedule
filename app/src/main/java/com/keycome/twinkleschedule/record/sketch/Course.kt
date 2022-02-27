package com.keycome.twinkleschedule.record.sketch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keycome.twinkleschedule.record.span.Day

@Entity(tableName = "course")
data class Course(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    val courseId: Long,

    @ColumnInfo(name = "parent_schedule_id")
    val parentScheduleId: Long,

    val title: String,

    val day: Day,

    val section: List<Int>,

    val week: List<Int>,

    val teacher: String,

    val classroom: String,
)