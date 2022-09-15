package com.keycome.twinkleschedule.repository

import com.keycome.twinkleschedule.database.CourseDecorationDao
import com.keycome.twinkleschedule.database.TimetableDatabase
import com.keycome.twinkleschedule.record.timetable.CourseAppearance
import com.keycome.twinkleschedule.record.timetable.CourseDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CourseDecorationRepository {

    private val database: TimetableDatabase = TimetableDatabase.getInstance()
    private val courseDecorationDao: CourseDecorationDao = database.courseDecorationDao()

    suspend fun queryCoursesAppearance(scheduleId: Long): List<CourseAppearance> {
        return withContext(Dispatchers.IO) {
            courseDecorationDao.queryCoursesAppearance(scheduleId)
        }
    }

    suspend fun insertCourseDecoration(courseDecoration: CourseDecoration) {
        withContext(Dispatchers.IO) {
            courseDecorationDao.insertCourseDecoration(courseDecoration)
        }
    }

    suspend fun updateCourseDecoration(courseDecoration: CourseDecoration) {
        withContext(Dispatchers.IO) {
            courseDecorationDao.updateCourseDecoration(courseDecoration)
        }
    }
}