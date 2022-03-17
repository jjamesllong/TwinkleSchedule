package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellTimeLineDescriptionBinding
import com.keycome.twinkleschedule.record.sketch.TimeLine

class CheckTimeLineAdapter(
    private val event: (TimeLine) -> Unit
) : ListAdapter<TimeLine, CheckTimeLineAdapter.TimeLineDescription>(TimeLineDiffDescription) {

    object TimeLineDiffDescription : DiffUtil.ItemCallback<TimeLine>() {
        override fun areItemsTheSame(oldItem: TimeLine, newItem: TimeLine): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: TimeLine, newItem: TimeLine): Boolean {
            return oldItem.name == newItem.name
        }

    }

    class TimeLineDescription(
        val binding: CellTimeLineDescriptionBinding,
        val event: (TimeLine) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var timeLine: TimeLine? = null

        init {
            binding.root.setOnClickListener {
                timeLine?.let { event(it) }
            }
        }

        fun onBind(timeLine: TimeLine) {
            this.timeLine = timeLine
            binding.timeLineDescription.text = timeLine.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineDescription {
        val binding = CellTimeLineDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeLineDescription(binding, event)
    }

    override fun onBindViewHolder(holder: TimeLineDescription, position: Int) {
        holder.onBind(getItem(position))
    }
}