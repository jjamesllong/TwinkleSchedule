package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellSectionListBinding
import com.keycome.twinkleschedule.record.timetable.Section

class EditRoutineAdapter(
    private val clickAction: (View, Int) -> Unit
) : ListAdapter<Section, EditRoutineAdapter.EditSectionCell>(
    object : DiffUtil.ItemCallback<Section>() {
        override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
            if (oldItem.order == newItem.order) {
                if (oldItem.from.toSeconds() == newItem.from.toSeconds()) {
                    if (oldItem.to.toSeconds() == newItem.to.toSeconds()) {
                        return true
                    }
                }
            }
            return false
        }

        override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
            if (oldItem.order == newItem.order) {
                if (oldItem.from.toSeconds() == newItem.from.toSeconds()) {
                    if (oldItem.to.toSeconds() == newItem.to.toSeconds()) {
                        return true
                    }
                }
            }
            return false
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditSectionCell {
        val binding = CellSectionListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditSectionCell(binding, clickAction)
    }

    override fun onBindViewHolder(holder: EditSectionCell, position: Int) {
        holder.bind(currentList[position])
    }

    class EditSectionCell(
        val binding: CellSectionListBinding,
        val clickAction: (View, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cellSectionListRoot.setOnClickListener { view ->
                clickAction(view, bindingAdapterPosition)
            }
            binding.cellSectionListDelete.setOnClickListener { view ->
                clickAction(view, bindingAdapterPosition)
            }
        }

        fun bind(section: Section) {
            val s = section.toString().split(delimiters = arrayOf("@"))
            binding.cellSectionListNumber.text = s[1]
            binding.cellSectionListContent.text = s[0]
        }
    }
}