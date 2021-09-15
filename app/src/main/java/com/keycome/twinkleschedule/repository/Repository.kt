package com.keycome.twinkleschedule.repository

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.model.sketch.Schedule
import com.keycome.twinkleschedule.database.ScheduleDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    private val database = ScheduleDatabase.getInstance()
    private val scheduleDao = database.scheduleDao()
    private val courseDao = database.courseDao()

    suspend fun insertSchedule(schedule: Schedule) {
        withContext(Dispatchers.IO) {
            scheduleDao.insertSchedule(schedule)
        }
    }

    fun queryAllSchedule(): LiveData<List<Schedule>> = scheduleDao.queryAllSchedule()
}