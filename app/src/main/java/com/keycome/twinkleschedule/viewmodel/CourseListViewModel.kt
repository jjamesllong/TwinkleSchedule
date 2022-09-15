package com.keycome.twinkleschedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.subscribe
import com.keycome.twinkleschedule.extension.asLiveData
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.repository.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseListViewModel : BaseViewModel() {

    var scheduleId = 0L

    var selectedIndex = -1
        private set

    private val _liveCourseList = MutableLiveData<List<Pair<Course, Boolean>>>()
    val liveCourseList = _liveCourseList.asLiveData()

    override suspend fun onPlace() {
        super.onPlace()
        viewModelScope.launch(Dispatchers.Default) {
            Pipette.forEvent.subscribe("") {
                if (scheduleId != 0L) {
                    queryCourseList(scheduleId)
                }
            }
        }
    }

    fun queryCourseList(scheduleId: Long) {
        viewModelScope.launch {
            val courseList = CourseRepository.queryCoursesOfSchedule(scheduleId)
            _liveCourseList.value = courseList.map { Pair(it, false) }
        }
    }

    fun refreshSelected(position: Int) {
        _liveCourseList.value?.also { oldList ->
            val newList = oldList.mapIndexed { index, oldPair ->
                val newPair = if (index == position) {
                    oldPair.copy(second = !oldPair.second)
                } else {
                    if (index == selectedIndex) oldPair.copy(second = false) else oldPair
                }
                newPair
            }
            selectedIndex = position
            _liveCourseList.value = newList
        }
    }

    fun requestCourse(): Course? =
        if (selectedIndex != -1) _liveCourseList.value?.get(selectedIndex)?.first else null

    suspend fun deleteCourse(course: Course) {
        CourseRepository.deleteCourse(course)
    }

}