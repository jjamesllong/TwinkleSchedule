package com.keycome.twinkleschedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.ViewEditDailyRoutineHeaderBinding
import com.keycome.twinkleschedule.record.interval.Date

class EditDailyRoutineHeaderAdapter(
    private val event: (View) -> Unit
) : RecyclerView.Adapter<EditDailyRoutineHeaderAdapter.HeaderView>() {

    private var initialized = false

    private var name: String? = null

    private var date: Date? = null

    private var duration = 0

    private var node = 0

    private var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderView {
        val binding = ViewEditDailyRoutineHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderView(parent.context, binding).apply {
            binding.headerDailyRoutineName.setOnClickListener(event)
            binding.headerStartDate.setOnClickListener(event)
            binding.headerCourseDuration.setOnClickListener(event)
        }
    }

    override fun onBindViewHolder(holder: HeaderView, position: Int) {
        name?.let { holder.bindName(it) }
        date?.let { holder.bindDate(it) }
        holder.bindDuration(duration)
        holder.bindNode(node, count)
    }

    override fun getItemCount() = if (initialized) 1 else 0

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

    fun refreshDuration(duration: Int) {
        this.duration = duration
        considerNotify()
    }

    fun refreshNode(node: Int, count: Int) {
        this.node = node
        this.count = count
        considerNotify()
    }

    class HeaderView(
        val context: Context,
        val binding: ViewEditDailyRoutineHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindName(name: String) {
            binding.headerDailyRoutineNameText.text = name
        }

        fun bindDate(date: Date) {
            binding.headerStartDateText.text = date.toDotDateString()
        }

        fun bindDuration(duration: Int) {
            binding.headerCourseDurationText.text = duration.toString()
        }

        fun bindNode(node: Int, count: Int) {
            binding.headerNodeCountText.text = context.getString(
                R.string.adapter_edit_daily_routine_header_node,
                node,
                count
            )
        }
    }
}