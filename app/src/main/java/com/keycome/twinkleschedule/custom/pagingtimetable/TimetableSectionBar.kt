package com.keycome.twinkleschedule.custom.pagingtimetable

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.record.timetable.Section

class TimetableSectionBar(recyclerView: RecyclerView) {

    val adapter = SectionAdapter().also {
        recyclerView.adapter = it
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
    }

    fun refreshSectionList(sectionList: List<Section>) {
        adapter.sectionList = sectionList
    }

}