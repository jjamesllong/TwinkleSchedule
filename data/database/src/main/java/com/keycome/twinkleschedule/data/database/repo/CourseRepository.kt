package com.keycome.twinkleschedule.data.database.repo

import android.content.Context
import com.keycome.twinkleschedule.data.database.TimetableDatabase

class CourseRepository(context: Context) {

    val dao = TimetableDatabase.getDbBy(context).courseDao()

}