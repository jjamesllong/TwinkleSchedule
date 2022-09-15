package com.keycome.twinkleschedule.database

import androidx.room.*
import com.keycome.twinkleschedule.record.timetable.Schedule

@Dao
interface ScheduleDao {

    @Insert
    suspend fun insertSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedule ORDER BY start_date DESC")
    suspend fun queryAllSchedule(): List<Schedule>

    @Query("SELECT COUNT(schedule_id) FROM schedule")
    suspend fun queryScheduleCount(): Int

    @Query("DELETE FROM schedule")
    suspend fun deleteAllSchedule()

    @Query("SELECT * FROM schedule WHERE schedule_id = :scheduleId LIMIT 1")
    suspend fun queryScheduleById(scheduleId: Long): Schedule?

    @Query("DELETE FROM schedule WHERE schedule_id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Long)

    @Query("DELETE FROM course WHERE master_id = :masterId")
    suspend fun deleteCoursesOfSchedule(masterId: Long)

    @Query("DELETE FROM routine WHERE master_id = :masterId")
    suspend fun deleteRoutineOfSchedule(masterId: Long)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Query("SELECT COUNT(course_id) FROM course WHERE master_id = :scheduleId")
    suspend fun courseCount(scheduleId: Long): Int

    @Query("SELECT schedule_id FROM schedule")
    suspend fun queryScheduleIds(): List<Long>
}