package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellScheduleListBinding
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
        private val binding: CellScheduleListBinding,
        private val event: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cellScheduleManagerTitle.setOnClickListener { event(bindingAdapterPosition) }
        }

        fun onBind(schedule: Schedule) {
            binding.cellScheduleManagerTitle.text = schedule.scheduleName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleItem {
        val binding = CellScheduleListBinding.inflate(
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