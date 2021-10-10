package com.keycome.twinkleschedule.repository

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.database.ScheduleDatabase
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.Schedule
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

    fun queryCourseByParent(parentScheduleId: Long, week: Int): LiveData<List<Course>> =
        courseDao.queryCourseByParent(parentScheduleId, week)

    fun queryScheduleById(scheduleId: Long): LiveData<Schedule> =
        scheduleDao.queryScheduleById(scheduleId)
}