package com.keycome.twinkleschedule.custom.courseschedule

import androidx.recyclerview.widget.RecyclerView

abstract class TableAdapter<VH : RecyclerView.ViewHolder, T> : RecyclerView.Adapter<VH>() {
    abstract fun onSubmitTableData(data: T)
}