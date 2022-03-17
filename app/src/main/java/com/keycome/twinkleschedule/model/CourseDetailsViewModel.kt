package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.base.BaseViewModel2
import com.keycome.twinkleschedule.record.sketch.Course
import com.keycome.twinkleschedule.share.ShareOnlyVariable

class CourseDetailsViewModel : BaseViewModel2() {

    val sharedCourse: LiveData<Course>? by ShareOnlyVariable(
        shareSpace,
        DisplayCoursesViewModel.SHARED_COURSE
    )

    override fun onCleared() {
        super.onCleared()
        shareSpace.releaseReference(DisplayCoursesViewModel.SHARED_COURSE)
    }
}