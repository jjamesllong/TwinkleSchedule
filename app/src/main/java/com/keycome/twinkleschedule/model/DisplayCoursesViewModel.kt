package com.keycome.twinkleschedule.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.pipette.DisplayCoursesPipette
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.horizon.HorizonDifference
import com.keycome.twinkleschedule.record.sketch.CourseSchedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository

class DisplayCoursesViewModel : BaseViewModel() {

    val pipette: DisplayCoursesPipette by pipettes()

    private val _liveWeekNow = MutableLiveData<Int>()
    val liveWeekNow: LiveData<Int> get() = _liveWeekNow

    val liveCourseSchedule: LiveData<CourseSchedule> by lazy {
        Transformations.switchMap(GlobalPreference.displayScheduleId) { id ->
            val liveParentSchedule =
                CourseScheduleRepository.queryScheduleById(id)
            Transformations.switchMap(liveParentSchedule) { schedule ->
                val weekNow = HorizonDifference.weeklyDiff(
                    schedule.schoolBeginDate.toMillis(),
                    System.currentTimeMillis()
                ) + 1
                val liveCourseList =
                    CourseScheduleRepository.queryCourseByParent(schedule.scheduleId, weekNow)
                Transformations.switchMap(liveCourseList) { courseList ->
                    _liveWeekNow.value = weekNow
                    MutableLiveData<CourseSchedule>().apply {
                        value = CourseSchedule(schedule, courseList)
                    }
                }
            }
        }
    }

    override fun onInit() {
        pipette.modelLiveData = liveCourseSchedule
    }

    override suspend fun onAsync() {
        Log.d("DisplayCoursesViewModel", "onAsync()")
    }

}