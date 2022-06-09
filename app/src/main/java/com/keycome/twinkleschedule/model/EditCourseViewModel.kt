package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.subscribe
import com.keycome.twinkleschedule.dialog.*
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.repository.CourseRepository
import com.keycome.twinkleschedule.util.const.KEY_COURSE_SECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditCourseViewModel : BaseViewModel() {

    private var courseID = 0L

    private var parentScheduleID = 0L

    private val _liveTitle = MutableLiveData<String>()
    val liveTitle: LiveData<String> get() = _liveTitle

    private val _liveDay = MutableLiveData<Day>()
    val liveDay: LiveData<Day> get() = _liveDay

    private val _liveSection = MutableLiveData<List<Int>>()
    val liveSection: LiveData<List<Int>> get() = _liveSection

    private val _liveWeek = MutableLiveData<List<Int>>()
    val liveWeek: LiveData<List<Int>> get() = _liveWeek

    private val _liveTeacher = MutableLiveData<String>()
    val liveTeacher: LiveData<String> get() = _liveTeacher

    private val _liveClassroom = MutableLiveData<String>()
    val liveClassroom: LiveData<String> get() = _liveClassroom

    override suspend fun onPlace() {
        super.onPlace()
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                Pipette.forString.subscribe("edit_course_name") {
                    withContext(Dispatchers.Main) {
                        _liveTitle.value = it
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(CourseDayDialog.DayInItem) {
                    withContext(Dispatchers.Main) {
                        _liveDay.value = Day.fromNumber(it)
                    }
                }
            }
            launch {
                launch {
                    Pipette.forString.subscribe(KEY_COURSE_SECTION) {
                        val section = it.split(CourseSectionDialog.SEPARATOR)
                        val start = section[0].toInt()
                        val end = section[1].toInt()
                        withContext(Dispatchers.Main) {
                            _liveSection.value = (start..end).toList()
                        }
                    }
                }
            }
            launch {
                val list = mutableListOf<Int>()
                Pipette.forInt.collect {
                    when (it.first) {
                        CourseWeekDialog.Start_EMITING_TAG -> {
                            list.clear()
                        }
                        CourseWeekDialog.COURSE_WEEK_TAG -> {
                            list.add(it.second)
                        }
                        CourseWeekDialog.END_EMITTING_TAG -> {
                            withContext(Dispatchers.Main) {
                                _liveWeek.value = list
                            }
                        }
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(TeacherNameDialog.TEACHER_NAME_TAG) {
                    withContext(Dispatchers.Main) {
                        _liveTeacher.value = it
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(ClassroomDialog.CLASSROOM_TAG) {
                    withContext(Dispatchers.Main) {
                        _liveClassroom.value = it
                    }
                }
            }
        }
    }

    fun setParentScheduleId(id: Long) {
        parentScheduleID = id
    }

    fun setCourseInfo(
        courseId: Long,
        parentScheduleId: Long,
        title: String,
        day: Day,
        section: List<Int>,
        week: List<Int>,
        teacher: String,
        classroom: String
    ) {
        courseID = courseId
        parentScheduleID = parentScheduleId
        _liveTitle.value = title
        _liveDay.value = day
        _liveSection.value = section
        _liveWeek.value = week
        _liveTeacher.value = teacher
        _liveClassroom.value = classroom
    }

    suspend fun insertCourse(): Boolean {
        return withContext(Dispatchers.IO) {
            val course = requestCourse()
            course?.let {
                CourseRepository.insertCourse(it)
                true
            } ?: false
        }
    }

    suspend fun updateCourse(): Boolean {
        return withContext(Dispatchers.IO) {
            val course = requestCourse()
            course?.let {
                CourseRepository.updateCourse(it)
                true
            } ?: false
        }
    }

    private fun requestCourse(): Course? {
        val courseId = if (courseID == 0L) System.currentTimeMillis() else courseID
        if (parentScheduleID == 0L) return null
        val title = _liveTitle.value ?: ""
        val day = _liveDay.value ?: return null
        val section = _liveSection.value ?: return null
        val week = _liveWeek.value ?: return null
        val teacher = _liveTeacher.value ?: ""
        val classroom = _liveClassroom.value ?: ""
        return Course(courseId, parentScheduleID, title, day, section, week, teacher, classroom)
    }
}