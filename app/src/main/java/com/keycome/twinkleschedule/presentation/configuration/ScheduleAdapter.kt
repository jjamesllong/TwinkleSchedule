package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.databinding.CustomScheduleDescriptionBinding

class ScheduleAdapter :
    ListAdapter<ScheduleEntity, ScheduleAdapter.ScheduleViewHolder>(ScheduleDiffCallback) {

    object ScheduleDiffCallback : DiffUtil.ItemCallback<ScheduleEntity>() {

        override fun areItemsTheSame(
            oldItem: ScheduleEntity,
            newItem: ScheduleEntity
        ): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(
            oldItem: ScheduleEntity,
            newItem: ScheduleEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    class ScheduleViewHolder(private val binding: CustomScheduleDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(title: String, position: Int) {
            binding.scheduleName.text = title
        }

        companion object {
            fun from(parent: ViewGroup): ScheduleViewHolder {
                val b = CustomScheduleDescriptionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ScheduleViewHolder(b)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.onBind(currentList[position].name, position)
    }
}