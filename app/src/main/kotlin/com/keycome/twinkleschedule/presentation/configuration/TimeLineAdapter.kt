package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.model.sketch.TimeLine
import com.keycome.twinkleschedule.databinding.CellTimeLineDescriptionBinding

class TimeLineAdapter(private val onClick: (CellTimeLineDescriptionBinding, Int, TimeLine) -> Unit) :
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
        private val onClick: (CellTimeLineDescriptionBinding, Int, TimeLine) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentTimeLine: TimeLine? = null

        init {
            binding.root.setOnClickListener { v ->
                currentTimeLine?.let { onClick(binding, v.id, it) }
            }
            binding.cellTimeLineDescriptionDeleteButton.setOnClickListener { v ->
                currentTimeLine?.let { onClick(binding, v.id, it) }
            }
        }

        fun onBindData(timeLine: TimeLine) {
            currentTimeLine = timeLine
            binding.timeLineDescription.text = timeLine.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val b = CellTimeLineDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeLineViewHolder(b, onClick)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.onBindData(getItem(position))
    }
}