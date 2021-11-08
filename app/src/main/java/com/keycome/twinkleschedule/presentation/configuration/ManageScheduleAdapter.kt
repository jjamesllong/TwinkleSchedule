package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellScheduleManagerBinding
import com.keycome.twinkleschedule.model.sketch.Schedule

class ManageScheduleAdapter :
    ListAdapter<Schedule, ManageScheduleAdapter.ScheduleItem>(ScheduleDiffCallback) {

    object ScheduleDiffCallback : DiffUtil.ItemCallback<Schedule>() {

        override fun areItemsTheSame(
            oldItem: Schedule,
            newItem: Schedule
        ): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(
            oldItem: Schedule,
            newItem: Schedule
        ): Boolean {
            return oldItem == newItem
        }
    }

    class ScheduleItem(
        val binding: CellScheduleManagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(schedule: Schedule) {
            binding.cellScheduleManagerTitle.text = schedule.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleItem {
        val b = CellScheduleManagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScheduleItem(b)
    }

    override fun onBindViewHolder(holder: ScheduleItem, position: Int) {
        holder.onBind(currentList[position])
    }
}