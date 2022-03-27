package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.ScheduleDatabase

object CourseRepository {

    private val database = ScheduleDatabase.getInstance()
    private val courseDao = database.courseDao()
}