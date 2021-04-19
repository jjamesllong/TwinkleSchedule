package com.keycome.twinkleschedule.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_entity")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    var courseId: Int,

    @ColumnInfo(name = "parent_schedule_id")
    var parentScheduleId: Int,

    var title: String,

    var day: Day,

    @Embedded
    var section: Section,

    @Embedded
    var week: Week,

    var continuity: Continuity,

    var teacher: String,

    var classroom: String,
)

data class Week(var startWeek: Int, var endWeek: Int)

data class Section(var startSection: Int, var endSection: Int)

enum class Day { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

enum class Continuity { Continuous, Odd, Even }