package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellEditTimeLineBinding
import com.keycome.twinkleschedule.record.span.Time

class EditTimeLineAdapter(
    private val onclick: (CellEditTimeLineBinding, Int, Int) -> Unit
) : ListAdapter<Time, EditTimeLineAdapter.AddTimeLineViewHolder>(TitleDiffCallback) {

    object TitleDiffCallback : DiffUtil.ItemCallback<Time>() {
        override fun areItemsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem == newItem
        }

    }

    class AddTimeLineViewHolder(
        private val binding: CellEditTimeLineBinding,
        onclick: (CellEditTimeLineBinding, Int, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onclick(binding, it.id, bindingAdapterPosition) }
            binding.cellAddTimeLineDeleteButton.setOnClickListener {
                onclick(binding, it.id, bindingAdapterPosition)
            }
        }

        fun onBind(time: Time) {
            binding.cellAddTimeLineTitleText.text = time.to24StyleString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTimeLineViewHolder {
        val b = CellEditTimeLineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddTimeLineViewHolder(b, onclick)
    }

    override fun onBindViewHolder(holder: AddTimeLineViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

}