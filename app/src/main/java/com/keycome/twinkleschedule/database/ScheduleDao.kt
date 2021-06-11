package com.keycome.twinkleschedule.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Query("SELECT * FROM schedule_entity ORDER BY school_begin_date DESC")
    fun queryAllSchedule(): LiveData<List<ScheduleEntity>>
}