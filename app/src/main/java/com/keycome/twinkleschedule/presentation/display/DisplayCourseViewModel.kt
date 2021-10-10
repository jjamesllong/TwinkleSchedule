package com.keycome.twinkleschedule.presentation.display

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.model.horizon.HorizonDifference
import com.keycome.twinkleschedule.model.sketch.CourseSchedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository

class DisplayCourseViewModel : ViewModel() {

    val liveCourseSchedule: LiveData<CourseSchedule>
        get() = Transformations.switchMap(App.displayScheduleId) { id ->
            val liveParentSchedule =
                CourseScheduleRepository.queryScheduleById(id)
            Transformations.switchMap(liveParentSchedule) { schedule ->
                val weekNow = HorizonDifference.weeklyDiff(
                    schedule.schoolBeginDate.toMillis(),
                    System.currentTimeMillis()
                )
                val liveCourseList =
                    CourseScheduleRepository.queryCourseByParent(schedule.scheduleId, weekNow)
                Transformations.switchMap(liveCourseList) { courseList ->
                    MutableLiveData<CourseSchedule>().also {
                        it.postValue(CourseSchedule(schedule, courseList))
                    }
                }
            }
        }
}