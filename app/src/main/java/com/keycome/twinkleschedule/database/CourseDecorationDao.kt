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

    @Query("SELECT course.*, course_decoration.color FROM course INNER JOIN course_decoration ON course.course_id = course_decoration.course_id WHERE course.parent_schedule_id = :scheduleId")
    suspend fun queryCoursesAppearance(scheduleId: Long): List<CourseAppearance>
}