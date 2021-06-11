package com.keycome.twinkleschedule.presentation.configuration

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.database.ScheduleEntity

class ScheduleAdapter :
    ListAdapter<ScheduleEntity, ScheduleAdapter.ScheduleViewHolder>(ScheduleDiffCallback) {

    object ScheduleDiffCallback : DiffUtil.ItemCallback<ScheduleEntity>() {

        override fun areItemsTheSame(
            oldItem: ScheduleEntity,
            newItem: ScheduleEntity
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ScheduleEntity,
            newItem: ScheduleEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}