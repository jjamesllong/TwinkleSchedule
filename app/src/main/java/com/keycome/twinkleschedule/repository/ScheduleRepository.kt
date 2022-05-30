package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.ScheduleDao
import com.keycome.twinkleschedule.database.TimetableDatabase
import com.keycome.twinkleschedule.record.timetable.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ScheduleRepository {

    private val database: TimetableDatabase = TimetableDatabase.getInstance()
    private val scheduleDao: ScheduleDao = database.scheduleDao()

    suspend fun querySchedules(): List<Schedule> {
        return withContext(Dispatchers.IO) { scheduleDao.queryAllSchedule() }
    }

    suspend fun querySchedule(id: Long): Schedule? {
        return withContext(Dispatchers.IO) {
            scheduleDao.acquireScheduleById(id)
        }
    }

    suspend fun deleteSchedule(id: Long) {
        withContext(Dispatchers.IO + NonCancellable) {
            launch { scheduleDao.deleteScheduleById(id) }
            launch { scheduleDao.deleteCoursesBelongSchedule(id) }
            launch { scheduleDao.deleteDailyRoutinesBelongSchedule(id) }
            Unit
        }
    }

    suspend fun deleteSchedules() {
        withContext(Dispatchers.IO + NonCancellable) {
            val ids = scheduleDao.queryScheduleIds()
            ids.forEach { deleteSchedule(it) }
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