package com.keycome.twinkleschedule.custom.pagingtimetable

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TimetableDayBar(recyclerView: RecyclerView) {

    val adapter = DayAdapter().also {
        recyclerView.adapter = it
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    fun refreshDayNumber(day: Int) {
        adapter.endDayNumber = day
    }
}