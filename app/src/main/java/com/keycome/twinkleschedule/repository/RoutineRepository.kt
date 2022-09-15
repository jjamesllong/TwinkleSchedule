package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.RoutineDao
import com.keycome.twinkleschedule.database.TimetableDatabase
import com.keycome.twinkleschedule.record.timetable.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RoutineRepository {

    private val database: TimetableDatabase = TimetableDatabase.getInstance()
    private val routineDao: RoutineDao = database.routineDao()

    suspend fun queryRoutinesOfSchedule(masterId: Long): List<Routine> {
        return withContext(Dispatchers.IO) {
            routineDao.queryRoutinesOfSchedule(masterId)
        }
    }

    suspend fun insertRoutine(routine: Routine) {
        routineDao.insertRoutine(routine)
    }

    suspend fun updateRoutine(routine: Routine) {
        routineDao.updateRoutine(routine)
    }
}