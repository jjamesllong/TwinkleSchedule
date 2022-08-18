package com.keycome.twinkleschedule.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.CourseDecoration
import com.keycome.twinkleschedule.record.timetable.DailyRoutine
import com.keycome.twinkleschedule.record.timetable.Schedule

@Database(
    entities = [Schedule::class, Course::class, DailyRoutine::class, CourseDecoration::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class TimetableDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao
    abstract fun courseDao(): CourseDao
    abstract fun dailyRoutineDao(): DailyRoutineDao
    abstract fun courseDecorationDao(): CourseDecorationDao

    companion object {

        private var INSTANCE: TimetableDatabase? = null

        private const val databaseName = "timetable_database"

        fun getInstance(): TimetableDatabase {
            return INSTANCE ?: synchronized(TimetableDatabase::class) {
                INSTANCE ?: Room.databaseBuilder(
                    App.context,
                    TimetableDatabase::class.java,
                    databaseName
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}