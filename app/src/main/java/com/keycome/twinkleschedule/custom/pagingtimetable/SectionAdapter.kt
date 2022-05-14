package com.keycome.twinkleschedule.custom.pagingtimetable

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.record.timetable.Section

class SectionAdapter : RecyclerView.Adapter<SectionAdapter.SectionView>() {

    var sectionList = listOf<Section>()
        set(param) {
            if (param.size == field.size) {
                for (i in param.indices) {
                    if (param[i] != field[i]) {
                        notifyDataSetChanged()
                        break
                    }
                }
            } else {
                field = param
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionView {
        val textView = LayoutInflater.from(parent.context).inflate(
            R.layout.cell_timetable_section_bar,
            parent,
            false
        ) as TextView
        return SectionView(textView)
    }

    override fun onBindViewHolder(holder: SectionView, position: Int) {
        val text = StringBuilder()
            .append(position + 1)
            .append("\n")
            .append(sectionList[position].from.to24StyleString())
        holder.textView.text = text
    }

    override fun getItemCount() = sectionList.size

    class SectionView(val textView: TextView) : RecyclerView.ViewHolder(textView)
}