package com.keycome.twinkleschedule.presentation.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.database.ScheduleEntity
import com.keycome.twinkleschedule.databinding.CellScheduleTitleBinding

class ScheduleListAdapter :
    ListAdapter<ScheduleEntity, ScheduleListAdapter.ScheduleViewHolder>(ScheduleDiffCallback) {

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

    class ScheduleViewHolder(private val binding: CellScheduleTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemSchedule: ScheduleEntity? = null

        init {
            binding.root.setOnClickListener { v ->
                itemSchedule?.let { s ->
                    val direction =
                        ScheduleListFragmentDirections.actionScheduleListFragmentToAddCourseFragment(
                            scheduleId = s.scheduleId
                        )
                    Navigation.findNavController(v).navigate(direction)
                }
            }
        }

        fun onBind(schedule: ScheduleEntity, position: Int) {
            itemSchedule = schedule
            binding.scheduleTitle.text = schedule.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val b = CellScheduleTitleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScheduleViewHolder(b)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.onBind(currentList[position], position)
    }
}