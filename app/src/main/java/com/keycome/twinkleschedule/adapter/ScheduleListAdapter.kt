package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellScheduleManagerBinding
import com.keycome.twinkleschedule.record.timetable.Schedule

class ScheduleListAdapter(
    private val event: (Int) -> Unit
) : ListAdapter<Schedule, ScheduleListAdapter.ScheduleItem>(ScheduleDiff) {

    object ScheduleDiff : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

    }

    class ScheduleItem(
        private val binding: CellScheduleManagerBinding,
        private val event: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { event(bindingAdapterPosition) }
        }

        fun onBind(schedule: Schedule) {
            binding.cellScheduleManagerTitle.text = schedule.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleItem {
        val binding = CellScheduleManagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScheduleItem(binding, event)
    }

    override fun onBindViewHolder(holder: ScheduleItem, position: Int) {
        holder.onBind(getItem(position))
    }
}