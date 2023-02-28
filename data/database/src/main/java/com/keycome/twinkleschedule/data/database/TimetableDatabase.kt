package com.keycome.twinkleschedule.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keycome.twinkleschedule.data.database.dao.*
import com.keycome.twinkleschedule.data.database.entity.*
import com.keycome.twinkleschedule.data.database.util.RecordConverter

@Database(
    entities = [
        CourseEntity::class,
        RoutineEntity::class,
        ScheduleEntity::class,
        SectionEntity::class,
        ZoneEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    value = [RecordConverter::class]
)
abstract class TimetableDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun routineDao(): RoutineDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun sectionDao(): SectionDao
    abstract fun zoneDao(): ZoneDao

    companion object {

        private const val databaseName = "timetable_db"

        @Volatile
        private var db: TimetableDatabase? = null

        fun instance(context: Context): TimetableDatabase {
            return db ?: synchronized(TimetableDatabase::class) {
                db ?: Room.databaseBuilder(
                    context.applicationContext,
                    TimetableDatabase::class.java,
                    databaseName
                ).build()
            }
        }
    }
}
