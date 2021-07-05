package com.keycome.twinkleschedule.presentation.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.database.TimeLine
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.Date
import com.keycome.twinkleschedule.model.Day
import com.keycome.twinkleschedule.model.LiveSchedule
import com.keycome.twinkleschedule.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigurationViewModel : ViewModel() {

    val liveScheduleList: LiveData<List<ScheduleEntity>> by lazy { Repository.queryAllSchedule() }

    val liveSchedule = LiveSchedule()

    fun insertSchedule() {
        GlobalScope.launch {
            Repository.insertSchedule(liveSchedule.value!!)
            launch(Dispatchers.Main) { toast("数据写入成功") }
        }
    }
}