package com.keycome.twinkleschedule.presentation.display

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CourseDescriptionBinding

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    class CourseViewHolder(val binding: CourseDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = CourseDescriptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val text = (position + 1).toString()
        holder.binding.textView.text = text
    }

    override fun getItemCount(): Int {
        return 45
    }
}