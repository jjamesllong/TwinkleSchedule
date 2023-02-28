package com.keycome.twinkleschedule.data.database.dao

import androidx.room.*
import com.keycome.twinkleschedule.data.database.entity.ScheduleEntity

@Dao
interface ScheduleDao {

    @Insert
    suspend fun insert(schedule: ScheduleEntity)

    @Update
    suspend fun update(schedule: ScheduleEntity)

    @Delete
    suspend fun delete(schedule: ScheduleEntity)

    @Query("SELECT * FROM t_schedule WHERE id = :id LIMIT 1")
    suspend fun findOneById(id: Long): ScheduleEntity?

}