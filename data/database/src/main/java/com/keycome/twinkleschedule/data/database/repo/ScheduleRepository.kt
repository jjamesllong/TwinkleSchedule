package com.keycome.twinkleschedule.data.database.repo

import android.content.Context
import com.keycome.twinkleschedule.core.record.timetable.Schedule
import com.keycome.twinkleschedule.data.database.TimetableDatabase
import com.keycome.twinkleschedule.data.database.util.RecordConverter

class ScheduleRepository(context: Context) {

    private val dao = TimetableDatabase.getDbBy(context).scheduleDao()

    private val cv = RecordConverter

    suspend fun insert(schedule: Schedule) = dao.insert(cv.fromSchedule(schedule))
}
