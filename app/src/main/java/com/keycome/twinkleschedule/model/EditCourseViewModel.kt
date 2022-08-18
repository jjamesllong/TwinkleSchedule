package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.subscribe
import com.keycome.twinkleschedule.dialog.CourseSectionDialog
import com.keycome.twinkleschedule.dialog.CourseWeekDialog
import com.keycome.twinkleschedule.extension.ints.restoreToPair
import com.keycome.twinkleschedule.extension.ints.toStringHex
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.CourseDecoration
import com.keycome.twinkleschedule.repository.CourseDecorationRepository
import com.keycome.twinkleschedule.repository.CourseRepository
import com.keycome.twinkleschedule.util.const.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditCourseViewModel : BaseViewModel() {

    var scheduleId = 0L
        private set

    var endDay = 0
        private set

    var endSection = 0
        private set

    var endWeek = 0
        private set

    private var courseID = 0L

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

    private val _liveColor = MutableLiveData<Int>()
    val liveColor: LiveData<Int> get() = _liveColor


    override suspend fun onPlace() {
        super.onPlace()
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                Pipette.forString.subscribe(KEY_COURSE_TITLE) {
                    withContext(Dispatchers.Main) {
                        _liveTitle.value = it
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(KEY_COURSE_DAY) {
                    withContext(Dispatchers.Main) {
                        _liveDay.value = Day.fromNumber(it)
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(CourseSectionDialog.KEY_SECTION_PAIR) {
                    val sectionPair = it.restoreToPair()
                    val start = sectionPair.first
                    val end = sectionPair.second
                    withContext(Dispatchers.Main) {
                        _liveSection.value = (start..end).toList()
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(CourseWeekDialog.KEY_SELECTED_COURSE_WEEKS) {
                    val list = mutableListOf<Int>()
                    var i = 0
                    while (i < endWeek) {
                        if (it and (1 shl i) > 0) {
                            val week = i + 1
                            list.add(week)
                        }
                        i++
                    }
                    withContext(Dispatchers.Main) {
                        _liveWeek.value = list
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(KEY_COURSE_TEACHER) {
                    withContext(Dispatchers.Main) {
                        _liveTeacher.value = it
                    }
                }
            }
            launch {
                Pipette.forString.subscribe(KEY_COURSE_CLASSROOM) {
                    withContext(Dispatchers.Main) {
                        _liveClassroom.value = it
                    }
                }
            }
            launch {
                Pipette.forInt.subscribe(KEY_COURSE_COLOR) {
                    withContext(Dispatchers.Main) {
                        _liveColor.value = it
                    }
                }
            }
        }
    }

    fun setScheduleLimit(scheduleId: Long, endDay: Int, endSection: Int, endWeek: Int) {
        this.scheduleId = scheduleId
        this.endDay = endDay
        this.endSection = endSection
        this.endWeek = endWeek
    }

    fun setCourseInfo(
        courseId: Long,
        title: String,
        day: Day,
        section: List<Int>,
        week: List<Int>,
        teacher: String,
        classroom: String
    ) {
        courseID = courseId
        _liveTitle.value = title
        _liveDay.value = day
        _liveSection.value = section
        _liveWeek.value = week
        _liveTeacher.value = teacher
        _liveClassroom.value = classroom
    }

    suspend fun insertCourse(): Boolean {
        return withContext(Dispatchers.IO) {
            requestCourse()?.let { course ->
                requestCourseDecoration(course.courseId)?.let { decoration ->
                    CourseRepository.insertCourse(course)
                    CourseDecorationRepository.insertCourseDecoration(decoration)
                    true
                } ?: false
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

    fun getSelectedWeek(): Int {
        val list = liveWeek.value ?: emptyList()
        var selectedWeek = 0
        list.map { selectedWeek = selectedWeek or (1 shl (it - 1)) }
        return selectedWeek
    }

    private fun requestCourse(): Course? {
        val courseId = if (courseID == 0L) System.currentTimeMillis() else courseID
        val parentScheduleId = if (scheduleId == 0L) return null else scheduleId
        val title = _liveTitle.value ?: return null
        val day = _liveDay.value ?: return null
        val section = _liveSection.value ?: return null
        val week = _liveWeek.value ?: return null
        val teacher = _liveTeacher.value ?: return null
        val classroom = _liveClassroom.value ?: return null
        return Course(courseId, parentScheduleId, title, day, section, week, teacher, classroom)
    }

    private fun requestCourseDecoration(id: Long): CourseDecoration? {
        val color = _liveColor.value ?: return null
        return CourseDecoration(id, color.toStringHex(prefix = "0x"))
    }
}