package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.ViewEditRoutineHeaderBinding
import com.keycome.twinkleschedule.record.interval.Date

class EditRoutineHeaderAdapter(
    private val clickAction: (View) -> Unit
) : RecyclerView.Adapter<EditRoutineHeaderAdapter.RoutineHeaderView>() {

    private var initialized = false

    private var name: String? = null

    private var date: Date? = null

    private var duration: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineHeaderView {
        val binding = ViewEditRoutineHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoutineHeaderView(binding).apply {
            binding.viewEditRoutineNameItem.setOnClickListener(clickAction)
            binding.viewEditRoutineStartDateItem.setOnClickListener(clickAction)
            binding.viewEditRoutineDurationItem.setOnClickListener(clickAction)
        }
    }

    override fun onBindViewHolder(holder: RoutineHeaderView, position: Int) {
        name?.let { holder.bindName(it) }
        date?.let { holder.bindDate(it) }
        duration?.let { holder.bindDuration(it) }
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

    class RoutineHeaderView(
        val binding: ViewEditRoutineHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindName(name: String) {
            binding.viewEditRoutineName.text = name
        }

        fun bindDate(date: Date) {
            binding.viewEditRoutineStartDate.text = date.toDotDateString()
        }

        fun bindDuration(duration: Int) {
            binding.viewEditRoutineDuration.text = duration.toString()
        }
    }
}