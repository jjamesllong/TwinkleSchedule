package com.keycome.twinkleschedule.presentation.display

import androidx.lifecycle.*
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.custom.courseschedule.ViewBlockFactory
import com.keycome.twinkleschedule.model.horizon.HorizonDifference
import com.keycome.twinkleschedule.model.sketch.CourseSchedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    CourseScheduleRepository.queryCourseByParent(schedule.scheduleId)
                Transformations.switchMap(liveCourseList) { courseList ->
                    MutableLiveData<CourseSchedule>().also {
                        viewModelScope.launch {
                            val orderedList = withContext(Dispatchers.Default) {
                                ViewBlockFactory.selectToGroupAndSort(
                                    courseList, weekNow
                                )
                            }
                            it.postValue(CourseSchedule(schedule, orderedList))
                        }
                    }
                }
            }
        }
}