package com.keycome.twinkleschedule.presentation.configuration

import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.LiveSchedule
import com.keycome.twinkleschedule.repository.CourseScheduleRepository
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigurationViewModel : ViewModel() {

    val liveSchedule = LiveSchedule()

    fun insertSchedule(toDisplay: Boolean) {
        App.applicationScope.launch {
            val scheduleCount: Int = CourseScheduleRepository.queryScheduleCount()
            CourseScheduleRepository.insertSchedule(liveSchedule.value)
            val scheduleCount2: Int = CourseScheduleRepository.queryScheduleCount()
            launch(Dispatchers.Main) { toast("$scheduleCount, $scheduleCount2") }
            if (scheduleCount == 0 || toDisplay) {
                launch(Dispatchers.Main) {
                    val kv = MMKV.defaultMMKV()
                    val id = liveSchedule.value.scheduleId
                    kv.encode("display_schedule_id", id)
                    toast("$id")
                }
            }
            launch(Dispatchers.Main) { toast("数据写入成功") }
        }
    }
}