package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.TimetableDatabase
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.timetable.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CourseRepository {

    private val database = TimetableDatabase.getInstance()
    private val courseDao = database.courseDao()

    suspend fun insertCourse(course: Course) {
        withContext(Dispatchers.IO) {
            courseDao.insertCourse(course)
        }
    }

    suspend fun deleteCourse(course: Course) {
        withContext(Dispatchers.IO) {
            courseDao.deleteCourse(course)
        }
    }

    suspend fun updateCourse(course: Course) {
        withContext(Dispatchers.IO) {
            courseDao.updateCourse(course)
        }
    }

    suspend fun queryCoursesOfWeek(scheduleId: Long, week: Int): List<Course> {
        return withContext(Dispatchers.IO) {
            courseDao.queryCoursesOfWeek(scheduleId, week)
        }
    }
}