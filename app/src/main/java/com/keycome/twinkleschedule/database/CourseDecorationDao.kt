package com.keycome.twinkleschedule.database

import androidx.room.*
import com.keycome.twinkleschedule.record.timetable.CourseAppearance
import com.keycome.twinkleschedule.record.timetable.CourseDecoration

@Dao
interface CourseDecorationDao {

    @Insert
    suspend fun insertCourseDecoration(decoration: CourseDecoration)

    @Update
    suspend fun updateCourseDecoration(decoration: CourseDecoration)

    @Delete
    suspend fun deleteCourseDecoration(decoration: CourseDecoration)

    @Query("SELECT course.*, course_decoration.color, course_decoration.text_color FROM course INNER JOIN course_decoration ON course.course_id = course_decoration.course_id WHERE course.master_id = :masterId")
    suspend fun queryCoursesAppearance(masterId: Long): List<CourseAppearance>
}