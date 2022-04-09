package com.keycome.twinkleschedule.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.Schedule

@Database(
    entities = [Schedule::class, Course::class],
    version = 14,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao
    abstract fun courseDao(): CourseDao

    companion object {

        private const val databaseName = "schedule_database"
        private var INSTANCE: ScheduleDatabase? = null

        fun getInstance(): ScheduleDatabase {
            return INSTANCE ?: synchronized(ScheduleDatabase::class) {
                INSTANCE ?: Room.databaseBuilder(
                    App.context,
                    ScheduleDatabase::class.java,
                    databaseName
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}