package com.keycome.twinkleschedule.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insertSchedule(schedule: CourseSchedule)
}