package com.keycome.twinkleschedule.custom.courseschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellCourseDescriptionBinding
import com.keycome.twinkleschedule.model.ViewBlock
import com.keycome.twinkleschedule.model.sketch.Course

class CourseAdapter(
    private val daySpan: Int,
    private val courseList: List<Course>,
    private val viewBlockList: List<ViewBlock>,
    private val itemEvent: ((Course) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_PLACEHOLDER = -1
        const val TYPE_COURSE = 1
    }

    class CourseView(val binding: CellCourseDescriptionBinding, val view: View) :
        RecyclerView.ViewHolder(view)

    class Placeholder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val frameLayout = FrameLayout(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                parent.width / daySpan,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        return when (viewType) {
            TYPE_COURSE -> {
                val binding = CellCourseDescriptionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                CourseView(binding, frameLayout.apply { addView(binding.root) })
            }
            else -> Placeholder(frameLayout)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CourseView) {
            holder.apply {
                val index = viewBlockList[position].courseIndex
                val c = courseList[index]
                val courseInfoText = StringBuilder()
                    .append(c.title)
                    .append("\n@")
                    .append(c.classroom)
                    .toString()
                binding.courseInfo.text = courseInfoText
                view.setOnClickListener { itemEvent?.invoke(c) }
            }
        }
    }

    override fun getItemCount(): Int = viewBlockList.size

    override fun getItemViewType(position: Int): Int =
        if (viewBlockList[position].isCourse) TYPE_COURSE else TYPE_PLACEHOLDER
}