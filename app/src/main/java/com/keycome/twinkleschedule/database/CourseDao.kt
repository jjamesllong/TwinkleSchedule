package com.keycome.twinkleschedule.database

import androidx.room.Dao
import androidx.room.Insert
import com.keycome.twinkleschedule.model.sketch.Course

@Dao
interface CourseDao {
    @Insert
    suspend fun insertCourse(course: Course)
}