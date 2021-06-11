package com.keycome.twinkleschedule.database

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithSchedule(
    @Embedded
    var schedule: ScheduleEntity,
    @Relation(parentColumn = "schedule_id", entityColumn = "parent_schedule_id")
    var courseEntityList: List<CourseEntity>
)