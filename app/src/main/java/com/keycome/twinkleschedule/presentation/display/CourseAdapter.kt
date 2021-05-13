package com.keycome.twinkleschedule.presentation.display

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.database.CourseEntity
import com.keycome.twinkleschedule.databinding.CourseDescriptionBinding
import com.keycome.twinkleschedule.model.CourseBlock

class CourseAdapter(val daySpan: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_PLACEHOLDER = -1
        const val TYPE_COURSE = 1
    }

    class Course(view: View, val binding: CourseDescriptionBinding) : RecyclerView.ViewHolder(view)

    class Placeholder(view: View) : RecyclerView.ViewHolder(view)

    lateinit var courseBlockArray: Array<CourseBlock>
    lateinit var courseEntityArray: Array<CourseEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val frameLayout = FrameLayout(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                parent.width / daySpan,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        return when (viewType) {
            TYPE_COURSE -> {
                val binding = CourseDescriptionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                Course(frameLayout.apply { addView(binding.root) }, binding)
            }
            else -> Placeholder(frameLayout)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Course) {
            holder.apply {
                val index = courseBlockArray[position].courseIndex
                val courseInfoText = StringBuilder()
                    .append(courseEntityArray[index].title)
                    .append("\n@")
                    .append(courseEntityArray[index].classroom)
                    .toString()
                binding.courseInfo.text = courseInfoText
            }
        }
    }

    override fun getItemCount(): Int = courseBlockArray.size

    override fun getItemViewType(position: Int): Int =
        if (courseBlockArray[position].isCourse) TYPE_COURSE else TYPE_PLACEHOLDER
}