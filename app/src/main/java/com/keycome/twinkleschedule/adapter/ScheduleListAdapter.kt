package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellScheduleListBinding
import com.keycome.twinkleschedule.record.timetable.Schedule

class ScheduleListAdapter(
    private val itemClickCallback: (Int) -> Unit
) : ListAdapter<Schedule, ScheduleListAdapter.ScheduleItem>(
    object : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }
    }
) {

    private var usingScheduleId = 0L

    class ScheduleItem(
        private val binding: CellScheduleListBinding,
        private val clickCallback: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickCallback(bindingAdapterPosition)
            }
        }

        fun bind(schedule: Schedule) {
            binding.cellScheduleListTitle.text = schedule.scheduleName
        }

        fun showUseStatus() {
            binding.cellScheduleListUsing.visibility = View.VISIBLE
        }

        fun hideUseStatus() {
            binding.cellScheduleListUsing.visibility = View.INVISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleItem {
        val binding = CellScheduleListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScheduleItem(binding, itemClickCallback)
    }

    override fun onBindViewHolder(holder: ScheduleItem, position: Int) {
        val schedule = getItem(position)
        holder.bind(schedule)
        if (usingScheduleId == schedule.scheduleId) {
            holder.showUseStatus()
        } else {
            holder.hideUseStatus()
        }
    }

    fun setUsingScheduleId(id: Long) {
        usingScheduleId = id
        for (index in currentList.indices) {
            val schedule = currentList[index]
            if (id == schedule.scheduleId) {
                notifyItemChanged(index)
                break
            }
        }
    }
}