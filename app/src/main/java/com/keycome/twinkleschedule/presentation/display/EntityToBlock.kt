package com.keycome.twinkleschedule.presentation.display

import com.keycome.twinkleschedule.database.Day
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.model.CourseBlock

class EntityToBlock {
    val blockList = mutableListOf<CourseBlock>()
    fun convertEntityToBlock() {
        val array = TestData.courseArray
        var day = Day.Monday
        for (i in array.indices) {
            val c = array[i]
            if (c.day == day) {
            } else {
                day = c.day
            }
        }
    }
}