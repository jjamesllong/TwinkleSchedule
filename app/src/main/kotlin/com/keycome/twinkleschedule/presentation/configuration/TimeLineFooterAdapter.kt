package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.ViewTimeLineDescriptionFooterBinding

class TimeLineFooterAdapter(
    private val onClick: (View) -> Unit
) : RecyclerView.Adapter<TimeLineFooterAdapter.AddViewHolder>() {

    class AddViewHolder(binding: ViewTimeLineDescriptionFooterBinding, onClick: (View) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener(onClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val b = ViewTimeLineDescriptionFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddViewHolder(b, onClick)
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {}

    override fun getItemCount() = 1
}