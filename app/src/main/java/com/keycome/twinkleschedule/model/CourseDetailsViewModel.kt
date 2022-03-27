package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.record.sketch.Course
import com.keycome.twinkleschedule.delivery.ShareOnlyVariable

class CourseDetailsViewModel : BaseViewModel() {

    val sharedCourse: LiveData<Course>? by ShareOnlyVariable(
        shareSpace,
        DisplayCoursesViewModel.SHARED_COURSE
    )

    override fun onRemove() {
        super.onRemove()
        shareSpace.releaseReference(DisplayCoursesViewModel.SHARED_COURSE)
    }
}