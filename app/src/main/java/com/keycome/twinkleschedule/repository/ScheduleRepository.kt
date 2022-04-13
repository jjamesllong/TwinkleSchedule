package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.ScheduleDao
import com.keycome.twinkleschedule.database.ScheduleDatabase
import com.keycome.twinkleschedule.record.timetable.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ScheduleRepository {

    private val database: ScheduleDatabase = ScheduleDatabase.getInstance()
    private val scheduleDao: ScheduleDao = database.scheduleDao()

    suspend fun querySchedules(): List<Schedule> {
        return withContext(Dispatchers.IO) { scheduleDao.querySchedules() }
    }

    suspend fun querySchedule(id: Long): Schedule? {
        return withContext(Dispatchers.IO) {
            scheduleDao.queryScheduleByIdQuietly(id)
        }
    }

    suspend fun deleteSchedule(id: Long) {
        withContext(Dispatchers.IO) {
            launch {
                withContext(NonCancellable) {
                    scheduleDao.deleteScheduleById(id)
                }
            }
            launch {
                withContext(NonCancellable) {
                    scheduleDao.deleteCoursesBelongSchedule(id)
                }
            }
            Unit
        }
    }

    suspend fun updateSchedule(schedule: Schedule) {
        withContext(Dispatchers.IO) {
            scheduleDao.updateSchedule(schedule)
        }
    }

    suspend fun insertSchedule(schedule: Schedule) {
        withContext(Dispatchers.IO) {
            scheduleDao.insertSchedule(schedule)
        }
    }

    suspend fun isEmptySchedule(id: Long): Boolean {
        return scheduleDao.courseCount(id) == 0
    }
}