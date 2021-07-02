package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.database.TimeLine
import com.keycome.twinkleschedule.databinding.CellTimeLineDescriptionBinding

class TimeLineAdapter(private val onClick: (TimeLine) -> Unit) :
    ListAdapter<TimeLine, TimeLineAdapter.TimeLineViewHolder>(TimeLineDiffCallback) {

    object TimeLineDiffCallback : DiffUtil.ItemCallback<TimeLine>() {
        override fun areItemsTheSame(oldItem: TimeLine, newItem: TimeLine): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TimeLine, newItem: TimeLine): Boolean {
            return oldItem == newItem
        }


    }

    class TimeLineViewHolder(
        private val binding: CellTimeLineDescriptionBinding,
        private val onClick: (TimeLine) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentTimeLine: TimeLine? = null

        init {
            binding.root.setOnClickListener { currentTimeLine?.let { onClick(it) } }
        }

        fun onBindData(timeLine: TimeLine, position: Int) {
            currentTimeLine = timeLine
            binding.timeLineDescription.text = timeLine.name
        }

        companion object {
            fun from(parent: ViewGroup, onClick: (TimeLine) -> Unit): TimeLineViewHolder {
                val b = CellTimeLineDescriptionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TimeLineViewHolder(b, onClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.onBindData(getItem(position), position)
    }
}