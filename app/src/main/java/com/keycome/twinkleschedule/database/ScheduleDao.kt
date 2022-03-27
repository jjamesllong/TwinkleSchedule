package com.keycome.twinkleschedule.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.keycome.twinkleschedule.record.sketch.Schedule

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insertSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedule ORDER BY school_begin_date DESC")
    fun queryAllSchedule(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule ORDER BY school_begin_date DESC")
    suspend fun querySchedules(): List<Schedule>

    @Query("SELECT COUNT(schedule_id) FROM schedule")
    suspend fun queryScheduleCount(): Int

    @Query("DELETE FROM schedule")
    suspend fun deleteAllSchedule()

    @Query("SELECT * FROM schedule WHERE schedule_id = :scheduleId")
    fun queryScheduleById(scheduleId: Long): LiveData<Schedule>

    @Query("SELECT * FROM schedule WHERE schedule_id = :scheduleId")
    suspend fun queryScheduleByIdQuietly(scheduleId: Long): Schedule

    @Query("DELETE FROM schedule WHERE schedule_id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Long)

    @Query("DELETE FROM course WHERE parent_schedule_id = :scheduleId")
    suspend fun deleteCoursesBelongSchedule(scheduleId: Long)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Update
    suspend fun updateSchedule(schedule: Schedule)
}