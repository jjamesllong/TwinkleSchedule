package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.ViewTimeLineDescriptionFooterBinding

class CheckTimeLineFooterAdapter(
    private val event: (View) -> Unit
) : RecyclerView.Adapter<CheckTimeLineFooterAdapter.FooterViewHolder>() {

    class FooterViewHolder(
        binding: ViewTimeLineDescriptionFooterBinding,
        event: (View) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener(event)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        val binding = ViewTimeLineDescriptionFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FooterViewHolder(binding, event)
    }

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {}

    override fun getItemCount() = 1
}