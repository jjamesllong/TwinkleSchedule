package com.keycome.twinkleschedule.presentation.configuration

import androidx.lifecycle.ViewModel
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.extension.toast
import com.keycome.twinkleschedule.model.LiveSchedule
import com.keycome.twinkleschedule.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigurationViewModel : ViewModel() {

    val liveSchedule = LiveSchedule()

    fun insertSchedule() {
        App.applicationScope.launch {
            Repository.insertSchedule(liveSchedule.value)
            launch(Dispatchers.Main) { toast("数据写入成功") }
        }
    }
}