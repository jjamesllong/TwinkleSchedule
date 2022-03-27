package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.preference.GlobalPreference
import com.keycome.twinkleschedule.record.sketch.Course
import com.keycome.twinkleschedule.record.sketch.CourseSchedule
import com.keycome.twinkleschedule.record.sketch.Schedule
import com.keycome.twinkleschedule.record.span.SpanDifference
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import com.keycome.twinkleschedule.delivery.SharePostVariable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayCoursesViewModel : BaseViewModel() {

    val sharedCourse: MutableLiveData<Course> by SharePostVariable(shareSpace, SHARED_COURSE) {
        MutableLiveData()
    }

    private val _displayScheduleId = GlobalPreference.displayScheduleId
    val liveDisplayScheduleId get() = _displayScheduleId

    private val _liveParentSchedule = MutableLiveData<Schedule>()
    val liveParentSchedule: LiveData<Schedule> get() = _liveParentSchedule


    private val _liveCourseScheduleList = MutableLiveData<List<CourseSchedule>>()
    val liveCourseScheduleList: LiveData<List<CourseSchedule>> get() = _liveCourseScheduleList

    private val _liveWeekNow = MutableLiveData<Int>()
    val liveWeekNow: LiveData<Int> get() = _liveWeekNow

    private val _liveWeekSelected = MutableLiveData<Int>()
    val liveWeekSelected: LiveData<Int> get() = _liveWeekSelected

    private val _liveCourseList = MutableLiveData<List<Course>>()
    val liveCourseList: LiveData<List<Course>> get() = _liveCourseList

    private val _liveCourseSchedule = MutableLiveData<CourseSchedule>()
    val liveCourseSchedule: LiveData<CourseSchedule> get() = _liveCourseSchedule

    fun refreshDialogCourse(courseId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            liveCourseScheduleList.value?.let { courseScheduleList ->
                liveWeekSelected.value?.let { weekSelected ->
                    val course = courseScheduleList[weekSelected - 1].courseList.find {
                        it.courseId == courseId
                    }
                    sharedCourse.postValue(course)
                }
            }
        }
    }

    fun refreshParentSchedule(id: Long) {
        if (liveParentSchedule.value?.scheduleId == id)
            return
        viewModelScope.launch {
            val parentSchedule = withContext(Dispatchers.IO) {
                CourseScheduleRepository.queryScheduleByIdQuietly(id)
            }
            _liveParentSchedule.value = parentSchedule
        }
    }

    fun refreshWeekNow(schedule: Schedule) {
        val weekNow = SpanDifference.weeklyDiff(
            schedule.schoolBeginDate.toMillis(),
            System.currentTimeMillis()
        ) + 1
        if (liveWeekNow.value == weekNow)
            return
        _liveWeekNow.value = weekNow
    }

    fun refreshWeekSelected(week: Int) {
        if (liveWeekSelected.value == week)
            return
        _liveWeekSelected.value = week
    }

    fun refreshCourseList(week: Int) {
        _liveParentSchedule.value?.let { schedule ->
            viewModelScope.launch {
                val courseList = withContext(Dispatchers.IO) {
                    CourseScheduleRepository.queryCourseByParentQuietly(
                        schedule.scheduleId,
                        week
                    )
                }
                _liveCourseList.value = courseList
            }
        }
    }

    fun refreshCourseSchedule() {
        _liveParentSchedule.value?.let { schedule ->
            _liveCourseList.value?.let { courseList ->
                _liveCourseSchedule.value = CourseSchedule(schedule, courseList)
            }
        }
    }

    fun refreshCourseScheduleList(schedule: Schedule) {
        val condition = liveCourseScheduleList.value?.let {
            it[0].schedule.scheduleId == schedule.scheduleId
        } ?: false
        if (condition) return
        viewModelScope.launch {
            val lastWeek = withContext(Dispatchers.IO) {
                CourseScheduleRepository.queryLastWeek(schedule.scheduleId)
            }
            val scheduleCourseList = ArrayList<CourseSchedule>()
            for (i in 1..lastWeek) {
                val courseList = withContext(Dispatchers.IO) {
                    CourseScheduleRepository.queryCourseByParentQuietly(
                        schedule.scheduleId,
                        i
                    )
                }
                scheduleCourseList.add(CourseSchedule(schedule, courseList))
            }
            _liveCourseScheduleList.value = scheduleCourseList
        }
    }

    override fun onRemove() {
        super.onRemove()
        shareSpace.releaseReference(SHARED_COURSE)
    }

    companion object {
        const val SHARED_COURSE = "sharedCourse"
    }
}