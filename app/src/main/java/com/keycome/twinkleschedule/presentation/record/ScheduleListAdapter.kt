package com.keycome.twinkleschedule.presentation.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellScheduleTitleBinding
import com.keycome.twinkleschedule.model.sketch.Schedule

class ScheduleListAdapter(private val isManage: Boolean) :
    ListAdapter<Schedule, ScheduleListAdapter.ScheduleViewHolder>(ScheduleDiffCallback) {

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

    class ScheduleViewHolder(
        private val binding: CellScheduleTitleBinding,
        private val isManage: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        private var itemSchedule: Schedule? = null

        init {
            binding.root.setOnClickListener { v ->
                itemSchedule?.let { s ->
                    val direction = ScheduleListFragmentDirections
                    Navigation.findNavController(v).navigate(
                        when (isManage) {
                            true -> direction.actionScheduleListFragmentToManageCourseFragment(
                                scheduleId = s.scheduleId
                            )
                            else -> direction.actionScheduleListFragmentToAddCourseFragment(
                                scheduleId = s.scheduleId
                            )
                        }
                    )
                }
            }
        }

        fun onBind(schedule: Schedule, position: Int) {
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
        return ScheduleViewHolder(b, isManage)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.onBind(currentList[position], position)
    }
}