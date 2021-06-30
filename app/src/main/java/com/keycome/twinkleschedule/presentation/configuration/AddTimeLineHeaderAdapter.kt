package com.keycome.twinkleschedule.presentation.configuration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.ViewAddTimeLineHeaderBinding

class AddTimeLineHeaderAdapter(
    private val fragment: AddTimeLineFragment
) : RecyclerView.Adapter<AddTimeLineHeaderAdapter.HeaderViewHolder>() {
    class HeaderViewHolder(
        private val binding: ViewAddTimeLineHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBindClickListener(fragment: AddTimeLineFragment) {
            binding.headerAddTimeLine.setOnClickListener {  }
        }

        fun onBindData() {
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val b = ViewAddTimeLineHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderViewHolder(b)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val holder = HeaderViewHolder.from(parent)
        holder.onBindClickListener(fragment)
        return holder
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.onBindData()
    }

    override fun getItemCount() = 1
}