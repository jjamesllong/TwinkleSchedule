package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.record.timetable.TimeLine
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.interval.Time

class EditTimeLineViewModel : BaseViewModel() {

    var firstIn = true

    private val sharedTimeLine by shareOnlyVariable<MutableLiveData<MutableSet<TimeLine>>>(
        EditScheduleViewModel.sharedTimeLine
    )

    var timeLineId = -1L

    private val _liveEditingName by sharePostVariable(timeLineName) {
        MutableLiveData<String>()
    }
    val liveEditingName: LiveData<String> get() = _liveEditingName

    private val _liveEditingDate = MutableLiveData<Date>()
    val liveEditingDate: LiveData<Date> get() = _liveEditingDate

    private val _liveTimeNode = MutableLiveData(0)
    val liveTimeNode: LiveData<Int> get() = _liveTimeNode

    var dailyCourses = 0

    private val _liveEditingTimeList = MutableLiveData<MutableList<Time>>()
    val liveEditingTimeList: LiveData<MutableList<Time>> get() = _liveEditingTimeList

    override fun onRemove() {
        super.onRemove()
        release(timeLineName, EditScheduleViewModel.sharedTimeLine)
    }

    fun refreshName(name: String) {
        _liveEditingName.value = name
    }

    fun refreshDate(date: Date) {
        _liveEditingDate.value = date
    }

    fun refreshTimeList(list: MutableList<Time>) {
        _liveEditingTimeList.value = list
        _liveTimeNode.value = list.size
    }

    private fun deleteTime(list: MutableList<Time>, index: Int) {
        list.removeAt(index)
    }

    private fun insertTime(list: MutableList<Time>, time: Time) {
        val seconds = time.toSeconds()
        var index = -1
        for (i in list.indices) {
            val indexSeconds = list[i].toSeconds()
            if (seconds == indexSeconds) return
            if (seconds < indexSeconds) {
                index = i
                break
            }
        }
        if (index == -1) index = list.size
        list.add(index, time)
    }

    private fun updateTime(list: MutableList<Time>, oldTimeIndex: Int, newTime: Time) {
        if (list[oldTimeIndex].toSeconds() == newTime.toSeconds()) return
        deleteTime(list, oldTimeIndex)
        insertTime(list, newTime)
    }

    fun deleteTimeByIndex(index: Int) {
        _liveEditingTimeList.value?.let {
            deleteTime(it, index)
            refreshTimeList(it)
        }
    }

    fun insertTimeByTime(time: Time) {
        _liveEditingTimeList.value?.let {
            insertTime(it, time)
            refreshTimeList(it)
        }
    }

    fun updateTimeByIndex(oldTimeIndex: Int, newTime: Time) {
        _liveEditingTimeList.value?.let {
            updateTime(it, oldTimeIndex, newTime)
            refreshTimeList(it)
        }
    }

    fun checkTimeLineRight(): Boolean {
        if (_liveEditingName.value.isNullOrBlank())
            return false
        if (_liveEditingDate.value == null)
            return false
        if (dailyCourses == 0 || _liveTimeNode.value!! != dailyCourses)
            return false
        return true
    }

    fun submitTimeLine() {
        sharedTimeLine?.value?.let { set ->
            var timeLine: TimeLine? = null
            for (t in set) {
                if (t.id == timeLineId) {
                    timeLine = t
                    break
                }
            }
            timeLine?.let { set.remove(it) }
            if (timeLineId != -1L) {
                set.add(
                    TimeLine(
                        id = timeLineId,
                        name = _liveEditingName.value!!,
                        startDate = _liveEditingDate.value!!,
                        timeList = _liveEditingTimeList.value!!.toList()
                    )
                )
            }
        }
        sharedTimeLine?.value = sharedTimeLine?.value
    }

    companion object {
        const val timeLineName = "time_line_name"
    }
}