package com.keycome.twinkleschedule.presentation.display

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.span.SpanDifference
import com.keycome.twinkleschedule.record.sketch.Course
import com.keycome.twinkleschedule.record.sketch.CourseSchedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository

class DisplayViewModel : ViewModel() {

    val liveCourseSchedule: LiveData<CourseSchedule> by lazy {
        Transformations.switchMap(App.displayScheduleId) { id ->
            val liveParentSchedule =
                CourseScheduleRepository.queryScheduleById(id)
            Transformations.switchMap(liveParentSchedule) { schedule ->
                val weekNow = SpanDifference.weeklyDiff(
                    schedule.schoolBeginDate.toMillis(),
                    System.currentTimeMillis()
                ) + 1
                val liveCourseList =
                    CourseScheduleRepository.queryCourseByParent(schedule.scheduleId, 1)
                Transformations.switchMap(liveCourseList) { courseList ->
                    MutableLiveData<CourseSchedule>().also {
                        it.value = CourseSchedule(schedule, courseList)
                    }
                }
            }
        }
    }

    fun getCourseById(id: Long): Course? {
        return liveCourseSchedule.value?.courseList?.find {
            it.courseId == id
        }
    }
}