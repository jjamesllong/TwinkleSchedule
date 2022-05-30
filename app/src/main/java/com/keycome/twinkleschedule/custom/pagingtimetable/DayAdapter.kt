package com.keycome.twinkleschedule.custom.pagingtimetable

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.extension.toCharacter
import com.keycome.twinkleschedule.record.interval.Day

class DayAdapter : RecyclerView.Adapter<DayAdapter.DayView>() {

    class DayView(val textView: TextView) : RecyclerView.ViewHolder(textView)

    var endDayNumber = 0
        set(param) {
            if (field != param) {
                field = param
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val textView = LayoutInflater.from(parent.context).inflate(
            R.layout.cell_timetable_day_bar,
            parent,
            false
        ) as TextView
        if (endDayNumber != 0) {
            textView.layoutParams.width = parent.measuredWidth / endDayNumber
        }
        return DayView(textView)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.textView.text = Day.fromIndex(position).toCharacter()
    }

    override fun getItemCount() = endDayNumber

}