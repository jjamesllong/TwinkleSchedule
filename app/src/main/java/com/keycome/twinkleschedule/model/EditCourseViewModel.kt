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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditCourseViewModel : BaseViewModel() {

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
                Pipette.pipetteForString.subscribe("edit_course_name") {
                    withContext(Dispatchers.Main) {
                        _liveTitle.value = it
                    }
                }
            }
            launch {
                Pipette.pipetteForInt.subscribe(CourseDayDialog.DayInItem) {
                    withContext(Dispatchers.Main) {
                        _liveDay.value = Day.fromNumber(it)
                    }
                }
            }
            launch {
                var startCache = 0
                launch {
                    Pipette.pipetteForInt.subscribe(CourseSectionDialog.StartSection) {
                        startCache = it
                    }
                }
                launch {
                    Pipette.pipetteForInt.subscribe(CourseSectionDialog.EndSection) {
                        if (startCache != 0) {
                            withContext(Dispatchers.Main) {
                                _liveSection.value = (startCache..it).toList()
                            }
                        }
                    }
                }
            }
            launch {
                val list = mutableListOf<Int>()
                Pipette.pipetteForInt.collect {
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
                Pipette.pipetteForString.subscribe(TeacherNameDialog.TEACHER_NAME_TAG) {
                    withContext(Dispatchers.Main) {
                        _liveTeacher.value = it
                    }
                }
            }
            launch {
                Pipette.pipetteForString.subscribe(ClassroomDialog.CLASSROOM_TAG) {
                    withContext(Dispatchers.Main) {
                        _liveClassroom.value = it
                    }
                }
            }
        }
    }

    suspend fun insertCourse(parentScheduleId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val course = requestCourse(parentScheduleId)
            course?.let {
                CourseRepository.insertCourse(it)
                true
            } ?: false
        }
    }

    suspend fun updateCourse(parentScheduleId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val course = requestCourse(parentScheduleId)
            course?.let {
                CourseRepository.updateCourse(it)
                true
            } ?: false
        }
    }

    private fun requestCourse(parentScheduleId: Long): Course? {
        val courseId = System.currentTimeMillis()
        if (parentScheduleId == 0L) return null
        val title = _liveTitle.value ?: ""
        val day = _liveDay.value ?: return null
        val section = _liveSection.value ?: return null
        val week = _liveWeek.value ?: return null
        val teacher = _liveTeacher.value ?: ""
        val classroom = _liveClassroom.value ?: ""
        return Course(courseId, parentScheduleId, title, day, section, week, teacher, classroom)
    }
}