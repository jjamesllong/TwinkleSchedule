package com.keycome.twinkleschedule.record.timetable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_decoration")
data class CourseDecoration(
    @PrimaryKey
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    val color: String
)