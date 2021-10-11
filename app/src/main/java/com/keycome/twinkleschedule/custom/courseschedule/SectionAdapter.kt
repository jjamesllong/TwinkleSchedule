package com.keycome.twinkleschedule.custom.courseschedule

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.model.sketch.Schedule
import com.keycome.twinkleschedule.model.sketch.TimeLine

class SectionAdapter(val schedule: Schedule) : RecyclerView.Adapter<SectionAdapter.SectionView>() {

    class SectionView(view: FrameLayout) : RecyclerView.ViewHolder(view) {
        val textView = view[0] as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionView {
        val frameLayout = FrameLayout(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                parent.height / schedule.dailyCourses
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
        return SectionView(frameLayout)
    }

    override fun onBindViewHolder(holder: SectionView, position: Int) {
        val t: TimeLine? = schedule.timeLine.find { it.id == 0 }
        t?.let {
            val text = StringBuilder()
                .append(position + 1)
                .append("\n")
                .append(it.lineList[position].to24StyleString())
            holder.textView.text = text
        }
    }

    override fun getItemCount() = schedule.dailyCourses
}