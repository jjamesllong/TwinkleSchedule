package com.keycome.twinkleschedule.presentation.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.DISPLAY_SCHEDULE_ID
import com.keycome.twinkleschedule.model.LiveSchedule
import com.keycome.twinkleschedule.model.sketch.Schedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigurationViewModel : ViewModel() {

    val liveSchedule = LiveSchedule()

    val allScheduleLive: LiveData<List<Schedule>> by lazy {
        CourseScheduleRepository.queryAllSchedule()
    }

    suspend fun insertSchedule(toDisplay: Boolean) {
        withContext(Dispatchers.IO) {
            val scheduleCount: Int = CourseScheduleRepository.queryScheduleCount()
            CourseScheduleRepository.insertSchedule(liveSchedule.value)
            if (scheduleCount == 0 || toDisplay) {
                launch(Dispatchers.Main) {
                    val kv = MMKV.defaultMMKV()
                    val id = liveSchedule.value.scheduleId
                    kv.encode(DISPLAY_SCHEDULE_ID, id)
                    App.displayScheduleId.value = id
                }
            }
            launch(Dispatchers.Main) { toast("数据写入成功") }
        }
    }
}