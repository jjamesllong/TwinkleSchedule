package com.keycome.twinkleschedule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keycome.twinkleschedule.record.timetable.Routine

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine WHERE master_id = :masterId")
    suspend fun queryRoutinesOfSchedule(masterId: Long): List<Routine>

    @Query("DELETE FROM routine")
    suspend fun deleteAllRoutines()

    @Insert
    suspend fun insertRoutine(routine: Routine)

    @Update
    suspend fun updateRoutine(routine: Routine)
}