package com.keycome.twinkleschedule.widget.pagingtimetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.record.ViewBlock
import com.keycome.twinkleschedule.record.timetable.Course

class CourseAdapter(
    private val courseSelectedListener: (Course) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CourseView(
        val view: TextView,
        val courseSelectedListener: (Course) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private var _course: Course? = null

        init {
            view.setOnClickListener { _course?.let { c -> courseSelectedListener(c) } }
        }

        fun onBind(position: Int, courseList: List<Course>, viewBlock: List<ViewBlock>) {
            val course = courseList[viewBlock[position].courseIndex].also { _course = it }
            val text = StringBuilder()
                .append(course.title)
                .append("\n@")
                .append(course.classroom)
                .toString()
            view.text = text
        }
    }

    class Placeholder(view: View) : RecyclerView.ViewHolder(view)

    var courseList: List<Course>? = null
    var viewBlockList: List<ViewBlock>? = null
    var horizontalSpanCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PLACEHOLDER -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.cell_timetable_course_placeholder,
                    parent,
                    false
                )
                if (horizontalSpanCount != 0) {
                    view.layoutParams.width = parent.measuredWidth / horizontalSpanCount
                }
                Placeholder(view)
            }
            TYPE_COURSE -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.cell_timetable_course_view,
                    parent,
                    false
                ) as TextView
                if (horizontalSpanCount != 0) {
                    view.layoutParams.width = parent.measuredWidth / horizontalSpanCount
                }
                CourseView(view, courseSelectedListener)
            }
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CourseView && courseList != null && viewBlockList != null) {
            holder.onBind(position, courseList!!, viewBlockList!!)
        }
    }

    override fun getItemCount(): Int = viewBlockList?.size ?: 0

    override fun getItemViewType(position: Int): Int =
        viewBlockList?.let {
            if (it[position].isCourse) TYPE_COURSE else TYPE_PLACEHOLDER
        } ?: TYPE_UNDEFINED

    companion object {
        const val TYPE_UNDEFINED = -1
        const val TYPE_PLACEHOLDER = 0
        const val TYPE_COURSE = 1
    }
}

