package com.keycome.twinkleschedule.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.keycome.twinkleschedule.model.sketch.Schedule

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insertSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedule ORDER BY school_begin_date DESC")
    fun queryAllSchedule(): LiveData<List<Schedule>>
}