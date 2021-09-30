package com.keycome.twinkleschedule.presentation.display

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.model.horizon.Day
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.repository.Repository

class DisplayCourseViewModel(parentScheduleId: Long) : ViewModel() {

    val liveOrderedCourseList = Transformations.map(
        Repository.queryCourseByParent(parentScheduleId)
    ) {

    }

    fun sort1(list: MutableList<Course>): MutableList<Course> {
        val l = list.size
        var m = 0
        for (i in 0 until l - 1) {
            m = i
            for (j in i + 1 until l) {
                if (list[m].day.toNumber() > list[j].day.toNumber())
                    m = j
            }
            if (i != m) {
                val temp = list[i]
                list[i] = list[m]
                list[m] = temp
            }
        }
        return list
    }

    fun groupAndSort(sourceList: List<Course>): List<List<Course>> {
        val unorderedMap: Map<Day, List<Course>> = sourceList.groupBy { it.day }
        val unorderedList = mutableListOf<MutableList<Course>>()
        for (d in 0 until 7) {
            unorderedMap.forEach {
                if (it.key.ordinal == d)
                    unorderedList.add(it.value.toMutableList())
            }
        }
        unorderedList.forEach { list ->
            val l = list.size

            // bubble sort
            for (i in 0 until l - 1) {
                for (j in 0 until l - 1 - i) {
                    if (list[j].section.first() > list[j + 1].section.first()) {
                        val temp = list[j]
                        list[j] = list[j + 1]
                        list[j + 1] = temp
                    }
                }
            }

//            // selection sort
//            for (i in 0 until l - 1) {
//                var m = i
//                for (j in i + 1 until l) {
//                    if (list[j].section.first() > list[j + 1].section.first()) {
//                        m = j
//                    }
//                }
//                if (m != i) {
//                    val temp = list[i]
//                    list[i] = list[m]
//                    list[m] = temp
//                }
//            }
        }
        return unorderedList
    }
}