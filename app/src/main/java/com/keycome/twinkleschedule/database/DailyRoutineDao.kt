package com.keycome.twinkleschedule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keycome.twinkleschedule.record.timetable.DailyRoutine

@Dao
interface DailyRoutineDao {

    @Query("SELECT * FROM daily_routine WHERE parent_schedule_id = :parentScheduleId")
    suspend fun queryDailyRoutineList(parentScheduleId: Long): List<DailyRoutine>

    @Query("DELETE FROM daily_routine")
    suspend fun deleteAllDailyRoutine()

    @Insert
    suspend fun insertDailyRoutine(dailyRoutine: DailyRoutine)

    @Update
    suspend fun updateDailyRoutine(dailyRoutine: DailyRoutine)
}