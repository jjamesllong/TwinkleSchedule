package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.record.timetable.Section
import com.keycome.twinkleschedule.repository.CourseRepository
import com.keycome.twinkleschedule.repository.DailyRoutineRepository
import com.keycome.twinkleschedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayCoursesViewModel : BaseViewModel() {

    private val sharedCourse: MutableLiveData<Course> by sharePostVariable(SHARED_COURSE) {
        MutableLiveData()
    }

    private val _displayScheduleId = GlobalPreference.displayScheduleId
    val liveDisplayScheduleId: LiveData<Long> get() = _displayScheduleId

    private val _liveParentSchedule = MutableLiveData<Schedule>()
    val liveParentSchedule: LiveData<Schedule> get() = _liveParentSchedule

    private val _liveSectionList = MutableLiveData<List<Section>>()
    val liveSectionList: LiveData<List<Section>> get() = _liveSectionList

    private val _liveWeekNow = MutableLiveData<Int>()
    val liveWeekNow: LiveData<Int> get() = _liveWeekNow

    private val _liveWeekSelected = MutableLiveData<Int>()
    val liveWeekSelected: LiveData<Int> get() = _liveWeekSelected

    private val _livePagingCourseList = MutableLiveData<List<List<Course>?>>()
    val livePagingCourseList: LiveData<List<List<Course>?>> get() = _livePagingCourseList

    private val scheduleIdObserver = Observer<Long> {
        refreshParentSchedule(it)
    }

    private val parentScheduleObserver = Observer<Schedule> {
        refreshSectionList(it)
        refreshWeekNow(it)
    }

    private val weekNowObserver = Observer<Int> {
        if (it >= 1) {
            refreshWeekSelected(it)
        }
    }

    private val weekSelectedObserver = Observer<Int> { weekSelected ->
        liveParentSchedule.value?.let { schedule ->
            refreshWeeklyCourseList(schedule, weekSelected)
        }
    }

    override suspend fun onPlace() {
        super.onPlace()
        liveDisplayScheduleId.observeForever(scheduleIdObserver)
        liveParentSchedule.observeForever(parentScheduleObserver)
        liveWeekNow.observeForever(weekNowObserver)
        liveWeekSelected.observeForever(weekSelectedObserver)
    }

    override fun onRemove() {
        super.onRemove()
        liveDisplayScheduleId.removeObserver(scheduleIdObserver)
        liveParentSchedule.removeObserver(parentScheduleObserver)
        liveWeekNow.removeObserver(weekNowObserver)
        liveWeekSelected.removeObserver(weekSelectedObserver)
        shareSpace.releaseReference(SHARED_COURSE)
    }

    fun refreshDialogCourse(courseId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            livePagingCourseList.value?.let { pagingList ->
                liveWeekSelected.value?.let { weekSelected ->
                    val course = pagingList[weekSelected - 1]?.find { it.courseId == courseId }
                    course?.let {
                        withContext(Dispatchers.Main) {
                            sharedCourse.value = course
                        }
                    }
                }
            }
        }
    }

    fun refreshParentSchedule(id: Long) {
        if (liveParentSchedule.value?.scheduleId == id)
            return
        viewModelScope.launch {
            val parentSchedule = withContext(Dispatchers.IO) {
                ScheduleRepository.querySchedule(id)
            }
            parentSchedule?.let { _liveParentSchedule.value = it }
        }
    }

    fun refreshSectionList(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            val sectionList = DailyRoutineRepository.querySectionList(schedule)
            sectionList?.let {
                withContext(Dispatchers.Main) {
                    _liveSectionList.value = it
                }
            }
        }
    }

    fun refreshWeekNow(schedule: Schedule) {
        val weekNow = schedule.inferWeekNow()
        if (liveWeekNow.value == weekNow)
            return
        _liveWeekNow.value = weekNow
    }

    fun refreshWeekSelected(week: Int) {
        if (liveWeekSelected.value == week)
            return
        _liveWeekSelected.value = week
    }

    fun refreshWeeklyCourseList(schedule: Schedule, selectedWeek: Int) {
        if (livePagingCourseList.value != null) return
        val pagingCourseList = ArrayList<List<Course>?>()
        for (i in 0 until schedule.endWeek) {
            pagingCourseList.add(null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val currentCourseList =
                CourseRepository.queryCoursesOfWeek(schedule.scheduleId, selectedWeek)
            pagingCourseList[selectedWeek - 1] = currentCourseList
            withContext(Dispatchers.Main) {
                _livePagingCourseList.value = pagingCourseList
            }
            val newPagingCourseLise = ArrayList<List<Course>?>()
            for (i in 1..schedule.endWeek) {
                if (i != selectedWeek) {
                    val l = CourseRepository.queryCoursesOfWeek(schedule.scheduleId, i)
                    newPagingCourseLise.add(l)
                } else {
                    newPagingCourseLise.add(currentCourseList)
                }
            }
            withContext(Dispatchers.Main) {
                _livePagingCourseList.value = newPagingCourseLise
            }
        }
    }

    companion object {

        const val SHARED_COURSE = "sharedCourse"
    }
}