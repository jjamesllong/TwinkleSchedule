package com.keycome.twinkleschedule.custom.courseschedule

import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerTable<T> {

    var recyclerView: RecyclerView? = null

    var tableAdapter: TableAdapter<*, T>? = null

    open fun submitData(data: T) {
        tableAdapter?.onSubmitTableData(data)
    }

    protected inline fun configureRecyclerView(operation: RecyclerView.() -> Unit) {
        recyclerView?.let { operation(it) }
    }

}

