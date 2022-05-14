package com.keycome.twinkleschedule.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.DailyRoutine
import com.keycome.twinkleschedule.record.timetable.Schedule

@Database(
    entities = [Schedule::class, Course::class, DailyRoutine::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class TimetableDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao
    abstract fun courseDao(): CourseDao
    abstract fun dailyRoutineDao(): DailyRoutineDao

    companion object {

        private const val databaseName = "timetable_database"
        private var INSTANCE: TimetableDatabase? = null

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