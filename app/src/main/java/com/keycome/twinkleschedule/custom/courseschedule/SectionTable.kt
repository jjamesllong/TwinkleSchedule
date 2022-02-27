package com.keycome.twinkleschedule.custom.courseschedule

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.record.sketch.Schedule

class SectionTable(recyclerView: RecyclerView) : RecyclerTable<Schedule>() {
    init {
        super.recyclerView = recyclerView
        configureRecyclerView {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = SectionAdapter().also { super.tableAdapter = it }
        }
    }
}