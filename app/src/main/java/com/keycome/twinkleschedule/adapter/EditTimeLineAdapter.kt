package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellEditTimeLineBinding
import com.keycome.twinkleschedule.record.span.Time

class EditTimeLineAdapter(
    private val event: (View, Int) -> Unit
) : ListAdapter<Time, EditTimeLineAdapter.EditTimeLineCell>(Diff) {

    object Diff : DiffUtil.ItemCallback<Time>() {
        override fun areItemsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem.hour == newItem.hour && oldItem.minute == newItem.minute
        }

        override fun areContentsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem.hour == newItem.hour && oldItem.minute == newItem.minute
        }
    }

    class EditTimeLineCell(
        val binding: CellEditTimeLineBinding,
        val event: (View, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cellAddTimeLineRoot.setOnClickListener { view ->
                event(view, bindingAdapterPosition)
            }
            binding.cellAddTimeLineDeleteButton.setOnClickListener { view ->
                event(view, bindingAdapterPosition)
            }
        }

        fun onBind(time: Time) {
            binding.cellAddTimeLineTitleText.text = time.to24StyleString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTimeLineCell {
        val binding = CellEditTimeLineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditTimeLineCell(binding, event)
    }

    override fun onBindViewHolder(holder: EditTimeLineCell, position: Int) {
        holder.onBind(currentList[position])
    }
}