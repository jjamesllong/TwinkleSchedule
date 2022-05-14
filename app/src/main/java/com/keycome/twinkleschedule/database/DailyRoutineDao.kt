package com.keycome.twinkleschedule.database

import androidx.room.Dao
import androidx.room.Query
import com.keycome.twinkleschedule.record.timetable.DailyRoutine

@Dao
interface DailyRoutineDao {

    @Query("SELECT * FROM daily_routine WHERE parent_schedule_id = :parentScheduleId")
    suspend fun queryDailyRoutineList(parentScheduleId: Long): List<DailyRoutine>
}