package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.TimetableDatabase
import com.keycome.twinkleschedule.record.timetable.DailyRoutine
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.record.timetable.Section
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DailyRoutineRepository {

    private val database = TimetableDatabase.getInstance()
    private val dailyRoutineDao = database.dailyRoutineDao()

    suspend fun querySectionList(schedule: Schedule): List<Section>? {
        return withContext(Dispatchers.IO) {
            val dailyRoutines = dailyRoutineDao.queryDailyRoutineList(schedule.scheduleId)
            return@withContext when (dailyRoutines.size) {
                0 -> null
                1 -> dailyRoutines[0].routines
                else -> {
                    val sortedList = dailyRoutines.sortedByDescending {
                        it.startDate.toMilliSeconds()
                    }
                    val currentTimeMillis = System.currentTimeMillis()
                    sortedList.forEach {
                        if (it.startDate.toMilliSeconds() <= currentTimeMillis)
                            return@withContext it.routines
                    }
                    null
                }
            }
        }
    }

    suspend fun queryDailyRoutines(parentScheduleId: Long): List<DailyRoutine> {
        return withContext(Dispatchers.IO) {
            dailyRoutineDao.queryDailyRoutineList(parentScheduleId)
        }
    }
}