package com.keycome.twinkleschedule.repository

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.database.ScheduleDatabase
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CourseScheduleRepository {
    private val database = ScheduleDatabase.getInstance()
    private val scheduleDao = database.scheduleDao()
    private val courseDao = database.courseDao()

    suspend fun insertCourse(course: Course) {
        withContext(Dispatchers.IO) {
            courseDao.insertCourse(course)
        }
    }

    suspend fun insertSchedule(schedule: Schedule) {
        withContext(Dispatchers.IO) {
            scheduleDao.insertSchedule(schedule)
        }
    }

    suspend fun updateSchedule(schedule: Schedule) {
        withContext(Dispatchers.IO) {
            scheduleDao.updateSchedule(schedule)
        }
    }

    fun queryAllSchedule(): LiveData<List<Schedule>> = scheduleDao.queryAllSchedule()

    suspend fun queryScheduleCount(): Int {
        return withContext(Dispatchers.IO) {
            scheduleDao.queryScheduleCount()
        }
    }

    suspend fun deleteAllSchedule() {
        withContext(Dispatchers.IO) {
            scheduleDao.deleteAllSchedule()
        }
    }

    suspend fun queryCourseByParentQuietly(parentScheduleId: Long, week: Int): List<Course> =
        courseDao.queryCourseByParentQuietly(parentScheduleId, week)

    fun queryScheduleById(scheduleId: Long): LiveData<Schedule> =
        scheduleDao.queryScheduleById(scheduleId)

    suspend fun queryScheduleByIdQuietly(scheduleId: Long): Schedule? =
        scheduleDao.queryScheduleByIdQuietly(scheduleId)

    suspend fun deleteSchedule(schedule: Schedule) =
        scheduleDao.deleteSchedule(schedule)

    fun queryCourseOfParent(scheduleId: Long): LiveData<List<Course>> =
        courseDao.queryCourseOfParent(scheduleId)

    suspend fun deleteCourse(course: Course) = courseDao.deleteCourse(course)

    suspend fun queryLastWeek(scheduleId: Long) = courseDao.queryLastWeek(scheduleId)
}