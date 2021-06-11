package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CustomAddTimeLineItemBinding

class AddTimeLineAdapter :
    ListAdapter<String, AddTimeLineAdapter.AddTimeLineViewHolder>(TitleDiffCallback) {

    object TitleDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    class AddTimeLineViewHolder(private val binding: CustomAddTimeLineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {

            }
        }

        fun onBind(timeLineTitle: String, position: Int) {
            binding.cusomAddTimeLineTitleText.text = timeLineTitle
        }

        companion object {
            fun from(parent: ViewGroup): AddTimeLineViewHolder {
                val b = CustomAddTimeLineItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return AddTimeLineViewHolder(b)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTimeLineViewHolder {
        return AddTimeLineViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AddTimeLineViewHolder, position: Int) {
        holder.onBind(currentList[position], position)
    }

}