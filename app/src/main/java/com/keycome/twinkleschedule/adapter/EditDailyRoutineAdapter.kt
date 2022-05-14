package com.keycome.twinkleschedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.CellSectionListBinding
import com.keycome.twinkleschedule.record.timetable.Section

class EditDailyRoutineAdapter(
    private val event: (View, Int) -> Unit
) : ListAdapter<Section, EditDailyRoutineAdapter.EditSectionCell>(SectionDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditSectionCell {
        val binding = CellSectionListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditSectionCell(parent.context, binding, event)
    }

    override fun onBindViewHolder(holder: EditSectionCell, position: Int) {
        holder.onBind(currentList[position])
    }

    object SectionDiff : DiffUtil.ItemCallback<Section>() {
        override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
            return oldItem.from.toSeconds() == newItem.from.toSeconds()
        }

        override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
            return oldItem.from.toSeconds() == newItem.from.toSeconds()
        }

    }

    class EditSectionCell(
        val context: Context,
        val binding: CellSectionListBinding,
        val event: (View, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cellSectionRoot.setOnClickListener { view ->
                event(view, bindingAdapterPosition)
            }
            binding.cellSectionDeleteButton.setOnClickListener { view ->
                event(view, bindingAdapterPosition)
            }
        }

        fun onBind(section: Section) {
            val index = bindingAdapterPosition + 1
            val from = section.from.formatWithoutSecond24()
            val to = section.to.formatWithoutSecond24()
            binding.cellSectionTitleText.text = context.getString(
                R.string.adapter_edit_daily_routine_section,
                index,
                from,
                to
            )
        }
    }
}