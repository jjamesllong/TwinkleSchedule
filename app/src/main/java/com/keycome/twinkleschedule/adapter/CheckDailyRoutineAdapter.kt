package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellDailyRoutineDescriptionBinding
import com.keycome.twinkleschedule.record.timetable.DailyRoutine

class CheckDailyRoutineAdapter(
    private val event: (View, DailyRoutine) -> Unit
) : ListAdapter<DailyRoutine, CheckDailyRoutineAdapter.DailyRoutineItem>(DailyRoutineDiff()) {

    class DailyRoutineDiff : DiffUtil.ItemCallback<DailyRoutine>() {

        override fun areItemsTheSame(oldItem: DailyRoutine, newItem: DailyRoutine): Boolean {
            return oldItem.dailyRoutineId == newItem.dailyRoutineId
        }

        override fun areContentsTheSame(oldItem: DailyRoutine, newItem: DailyRoutine): Boolean {
            return oldItem.name == newItem.name
        }

    }

    class DailyRoutineItem(
        val binding: CellDailyRoutineDescriptionBinding,
        val event: (View, DailyRoutine) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var dailyRoutine: DailyRoutine? = null

        init {
            binding.dailyRoutineRoot.setOnClickListener {
                dailyRoutine?.apply { event(it, this) }
            }
            binding.dailyRoutineDelete.setOnClickListener {
                dailyRoutine?.apply { event(it, this) }
            }
        }

        fun onBind(dailyRoutine: DailyRoutine) {
            this.dailyRoutine = dailyRoutine
            binding.dailyRoutineDescription.text = dailyRoutine.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyRoutineItem {
        val binding = CellDailyRoutineDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DailyRoutineItem(binding, event)
    }

    override fun onBindViewHolder(holder: DailyRoutineItem, position: Int) {
        holder.onBind(getItem(position))
    }
}