package com.keycome.twinkleschedule.custom.courseschedule

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.extension.toCharacter
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Schedule

class DayAdapter : TableAdapter<DayAdapter.DayView, Schedule>() {

    class DayView(view: FrameLayout) : RecyclerView.ViewHolder(view) {
        val textView = view[0] as TextView
    }

    private var schedule: Schedule? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val frameLayout = FrameLayout(parent.context).apply {
            schedule?.let {
                layoutParams = FrameLayout.LayoutParams(
                    parent.width / it.weeklyEndDay.toNumber(),
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                addView(TextView(parent.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    gravity = Gravity.CENTER
                    setTextColor(
                        resources.getColor(R.color.text_color_secondary, null)
                    )
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                })
            }
        }
        return DayView(frameLayout)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.textView.text = Day.fromIndex(position).toCharacter()
    }

    override fun getItemCount() = schedule?.weeklyEndDay?.toNumber() ?: 0

    override fun onSubmitTableData(data: Schedule) {
        schedule = data
        notifyDataSetChanged()
    }
}