package com.keycome.twinkleschedule.pipette

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.base.Pipette
import com.keycome.twinkleschedule.record.sketch.CourseSchedule
import kotlinx.coroutines.channels.Channel

class DisplayCoursesPipette : Pipette() {
    val actionChannel = Channel<String>()
    val requestChannel = Channel<String>()

    // val modelChannel = Channel<LiveData<CourseSchedule>>()
    var modelLiveData: LiveData<CourseSchedule>? = null
}