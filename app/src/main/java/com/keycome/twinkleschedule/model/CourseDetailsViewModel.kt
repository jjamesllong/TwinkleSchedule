package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.record.timetable.Course

class CourseDetailsViewModel : BaseViewModel() {

    val sharedCourse: LiveData<Course>? by shareOnlyVariable<LiveData<Course>>(
        DisplayCoursesViewModel.SHARED_COURSE
    )

    override fun onRemove() {
        super.onRemove()
        release(DisplayCoursesViewModel.SHARED_COURSE)
    }
}