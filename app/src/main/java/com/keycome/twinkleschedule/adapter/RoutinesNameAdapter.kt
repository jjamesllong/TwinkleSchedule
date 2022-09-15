package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellRoutinesNameBinding
import com.keycome.twinkleschedule.record.timetable.Routine

class RoutinesNameAdapter(
    private val clickCallback: (View, Routine) -> Unit
) : ListAdapter<Routine, RoutinesNameAdapter.RoutinesNameItem>(
    object : DiffUtil.ItemCallback<Routine>() {
        override fun areItemsTheSame(oldItem: Routine, newItem: Routine): Boolean {
            return oldItem.routineId == newItem.routineId
        }

        override fun areContentsTheSame(oldItem: Routine, newItem: Routine): Boolean {
            return oldItem.routineName == newItem.routineName
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutinesNameItem {
        val binding = CellRoutinesNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoutinesNameItem(binding, clickCallback)
    }

    override fun onBindViewHolder(holder: RoutinesNameItem, position: Int) {
        holder.onBind(getItem(position))
    }

    class RoutinesNameItem(
        val binding: CellRoutinesNameBinding,
        val clickCallback: (View, Routine) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var routine: Routine? = null

        init {
            binding.cellRoutinesNameRoot.setOnClickListener { v ->
                routine?.also { r -> clickCallback(v, r) }
            }
            binding.cellRoutinesNameDelete.setOnClickListener { v ->
                routine?.also { r -> clickCallback(v, r) }
            }
        }

        fun onBind(routine: Routine) {
            this.routine = routine
            binding.cellRoutinesNameName.text = routine.routineName
        }
    }
}