package com.keycome.twinkleschedule.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.keycome.twinkleschedule.model.sketch.Course

@Dao
interface CourseDao {
    @Insert
    suspend fun insertCourse(course: Course)

    @Query("SELECT * FROM course WHERE parent_schedule_id = :parentScheduleId")
    fun queryCourseByParent(parentScheduleId: Long): LiveData<List<Course>>
}