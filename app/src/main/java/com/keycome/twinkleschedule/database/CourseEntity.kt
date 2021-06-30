package com.keycome.twinkleschedule.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keycome.twinkleschedule.model.Day

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

data class Week(
    @ColumnInfo(name = "start_week") var startWeek: Int,
    @ColumnInfo(name = "ene_week") var endWeek: Int
)

data class Section(
    @ColumnInfo(name = "start_section") var startSection: Int,
    @ColumnInfo(name = "end_section") var endSection: Int
)

enum class Continuity { Continuous, Odd, Even }