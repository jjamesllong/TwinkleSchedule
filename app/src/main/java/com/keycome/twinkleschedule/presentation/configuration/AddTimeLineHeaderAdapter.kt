package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.ViewAddTimeLineHeaderBinding
import com.keycome.twinkleschedule.model.sketch.Schedule

class AddTimeLineHeaderAdapter(
    var schedule: Schedule? = null,
    private val timeLineId: Int = 0,
    private val onClick: (ViewAddTimeLineHeaderBinding, Int) -> Unit
) : RecyclerView.Adapter<AddTimeLineHeaderAdapter.HeaderViewHolder>() {
    class HeaderViewHolder(
        private val binding: ViewAddTimeLineHeaderBinding,
        private val onClick: (ViewAddTimeLineHeaderBinding, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            for (view in binding.root.children)
                view.setOnClickListener { onClick(binding, it.id) }
        }

        fun onBindData(schedule: Schedule?, timeLineId: Int) {
            schedule?.let {
                it.timeLine.find { timeLine -> timeLine.id == timeLineId }?.let { timeLine ->
                    binding.headerCurrentTimeLineText.text = timeLine.name
                    binding.headerStartDateText.text = timeLine.startDate.toDotDateString()
                }
                binding.headerCourseDurationText.text = it.courseDuration.toString()
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClick: (ViewAddTimeLineHeaderBinding, Int) -> Unit
            ): HeaderViewHolder {
                val b = ViewAddTimeLineHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderViewHolder(b, onClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.onBindData(schedule, timeLineId)
    }

    override fun getItemCount() = if (schedule == null) 0 else 1
}