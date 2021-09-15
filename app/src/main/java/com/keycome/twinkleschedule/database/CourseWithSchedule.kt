package com.keycome.twinkleschedule.database

import androidx.room.Embedded
import androidx.room.Relation
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.Schedule

data class CourseWithSchedule(
    @Embedded
    var schedule: Schedule,
    @Relation(parentColumn = "schedule_id", entityColumn = "parent_schedule_id")
    var courseList: List<Course>
)