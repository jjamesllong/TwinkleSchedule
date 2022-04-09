package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellTimeLineDescriptionBinding

class CourseSectionAdapter : RecyclerView.Adapter<CourseSectionAdapter.ItemView>() {

    val list = (0..10).toList()

    class ItemView(val binding: CellTimeLineDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val b = CellTimeLineDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemView(b)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        holder.binding.timeLineDescription.text = list[position].toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}