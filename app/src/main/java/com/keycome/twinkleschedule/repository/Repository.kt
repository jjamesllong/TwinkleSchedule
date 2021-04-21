package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.CourseSchedule
import com.keycome.twinkleschedule.database.ScheduleDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    private val database = ScheduleDatabase.getInstance()
    private val scheduleDao = database.scheduleDao()
    private val courseDao = database.courseDao()

    suspend fun insertSchedule(schedule: CourseSchedule) {
        withContext(Dispatchers.IO) {
            scheduleDao.insertSchedule(schedule)
        }
    }
}