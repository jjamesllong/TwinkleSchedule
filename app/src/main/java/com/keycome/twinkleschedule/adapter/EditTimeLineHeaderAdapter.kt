package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.ViewEditTimeLineHeaderBinding
import com.keycome.twinkleschedule.record.interval.Date

class EditTimeLineHeaderAdapter(
    private val event: (View) -> Unit
) : RecyclerView.Adapter<EditTimeLineHeaderAdapter.HeaderView>() {

    class HeaderView(
        val binding: ViewEditTimeLineHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindName(name: String) {
            binding.headerCurrentTimeLineText.text = name
        }

        fun bindDate(date: Date) {
            binding.headerStartDateText.text = date.toDotDateString()
        }

        fun bindNode(node: Int, count: Int) {
            binding.headerNodeCountText.text = "$node / $count"
        }
    }

    private var initialized = false

    private var name: String? = null

    private var date: Date? = null

    private var node = 0

    private var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderView {
        val binding = ViewEditTimeLineHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderView(binding).apply {
            binding.headerCurrentTimeLine.setOnClickListener(event)
            binding.headerStartDate.setOnClickListener(event)
        }
    }

    override fun onBindViewHolder(holder: HeaderView, position: Int) {
        name?.let { holder.bindName(it) }
        date?.let { holder.bindDate(it) }
        holder.bindNode(node, count)
    }

    override fun getItemCount(): Int {
        return if (name == null) 0 else 1
    }

    private fun considerNotify() {
        if (initialized) {
            notifyItemChanged(0)
        } else {
            initialized = true
            notifyItemInserted(0)
        }
    }

    fun refreshName(name: String) {
        this.name = name
        considerNotify()
    }

    fun refreshDate(date: Date) {
        this.date = date
        considerNotify()
    }

    fun refreshNode(node: Int, count: Int) {
        this.node = node
        this.count = count
        considerNotify()
    }
}