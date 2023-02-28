package com.keycome.twinkleschedule.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keycome.twinkleschedule.data.database.dao.ScheduleDao
import com.keycome.twinkleschedule.data.database.entity.ScheduleEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DaoTest {

    private lateinit var db: TimetableDatabase
    private lateinit var scheduleDao: ScheduleDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TimetableDatabase::class.java).build()
        scheduleDao = db.scheduleDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun scheduleDaoIsCorrect() = runBlocking {
        val id = System.currentTimeMillis()
        val schedule = ScheduleEntity(id, "testSchedule", "2023-02-28", 10, 5, 16)
        scheduleDao.insert(schedule)
        val s1 = scheduleDao.findOneById(id)
        Assert.assertEquals(s1?.sectionsPerDay, 10)
        scheduleDao.update(schedule.copy(sectionsPerDay = 11))
        val s2 = scheduleDao.findOneById(id)
        Assert.assertEquals(s2?.sectionsPerDay, 11)
        scheduleDao.delete(schedule)
        val s3 = scheduleDao.findOneById(id)
        Assert.assertNull(s3)
    }
}