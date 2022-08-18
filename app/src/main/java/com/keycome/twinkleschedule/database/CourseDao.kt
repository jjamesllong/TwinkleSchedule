package com.keycome.twinkleschedule.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.keycome.twinkleschedule.record.timetable.Course

@Dao
interface CourseDao {

    @Insert
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Query(
        """
        select *
        from course
        where parent_schedule_id = :scheduleId and (week like '%,' || :week || ',%' or week like '[' || :week || ',%' or week like '%,' || :week || ']' or week like '[' || :week || ']')
        order by
        case day
        when 'Monday' then 1
        when 'Tuesday' then 2
        when 'Wednesday' then 3
        when 'Thursday' then 4
        when 'Friday' then 5
        when 'Saturday' then 6
        when 'Sunday' then 7
        else Null
        end,
        case
        when section like '[1,%' then 1
        when section like '[2,%' then 2
        when section like '[3,%' then 3
        when section like '[4,%' then 4
        when section like '[5,%' then 5
        when section like '[6,%' then 6
        when section like '[7,%' then 7
        when section like '[8,%' then 8
        when section like '[9,%' then 9
        when section like '[10,%' then 10
        when section like '[11,%' then 11
        when section like '[12,%' then 12
        when section like '[13,%' then 13
        when section like '[14,%' then 14
        when section like '[15,%' then 15
        when section like '[16,%' then 16
        else NULL
        end
        """
    )
    suspend fun queryCoursesOfWeek(scheduleId: Long, week: Int): List<Course>

    @Query("SELECT * FROM course WHERE parent_schedule_id = :scheduleId")
    suspend fun queryCoursesOfSchedule(scheduleId: Long): List<Course>

    @Query("select * from course where parent_schedule_id == :scheduleId")
    fun queryCourseOfParent(scheduleId: Long): LiveData<List<Course>>

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("DELETE FROM course")
    suspend fun deleteAllCourse()
}