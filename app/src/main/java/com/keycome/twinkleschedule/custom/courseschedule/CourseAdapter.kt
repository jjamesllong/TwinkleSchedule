package com.keycome.twinkleschedule.custom.courseschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellCourseDescriptionBinding
import com.keycome.twinkleschedule.record.ViewBlock
import com.keycome.twinkleschedule.record.sketch.Course
import com.keycome.twinkleschedule.record.sketch.CourseSchedule
import com.keycome.twinkleschedule.record.sketch.Schedule

class CourseAdapter : TableAdapter<RecyclerView.ViewHolder, CourseSchedule>() {

    companion object {
        const val TYPE_PLACEHOLDER = -1
        const val TYPE_UNDEFINED = 0
        const val TYPE_COURSE = 1
    }

    class CourseView(
        frameLayout: FrameLayout,
        private val courseAdapter: CourseAdapter
    ) : RecyclerView.ViewHolder(frameLayout) {

        var course: Course? = null
        val binding = CellCourseDescriptionBinding.inflate(
            LayoutInflater.from(frameLayout.context), frameLayout, false
        )

        init {
            frameLayout.addView(binding.root)
            binding.root.setOnClickListener {
                course?.let { _course ->
                    courseAdapter.onCourseViewClicked(_course)
                }
            }
        }
    }

    class Placeholder(view: View) : RecyclerView.ViewHolder(view)

    var daySpan: Int = 1
    var viewBlockList: List<ViewBlock>? = null
    var schedule: Schedule? = null
    var courseList: List<Course>? = null
    var courseTable: CourseTable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val frameLayout = FrameLayout(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                parent.width / daySpan,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        return when (viewType) {
            TYPE_COURSE -> CourseView(frameLayout, this)
            TYPE_PLACEHOLDER -> Placeholder(frameLayout)
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CourseView) {
            holder.apply {
                viewBlockList?.let { _viewBlockList ->
                    courseList?.let { _courseList ->
                        val index = _viewBlockList[position].courseIndex
                        val c = _courseList[index]
                        course = c
                        val courseInfoText = StringBuilder()
                            .append(c.title)
                            .append("\n@")
                            .append(c.classroom)
                            .toString()
                        binding.courseInfo.text = courseInfoText
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = viewBlockList?.size ?: 0

    override fun getItemViewType(position: Int): Int =
        viewBlockList?.let {
            if (it[position].isCourse) TYPE_COURSE else TYPE_PLACEHOLDER
        } ?: TYPE_UNDEFINED

    override fun onSubmitTableData(data: CourseSchedule) {
        notifyDataSetChanged()
    }

    fun onCourseViewClicked(course: Course) {
        courseTable?.onCourseViewClicked(course)
    }
}

