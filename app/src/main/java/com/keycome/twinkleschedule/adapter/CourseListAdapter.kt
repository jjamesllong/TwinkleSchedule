package com.keycome.twinkleschedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellCourseListBinding
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Course

class CourseListAdapter(
    private val event: (Int) -> Unit
) : ListAdapter<Pair<Course, Boolean>, CourseListAdapter.CourseItem>(CourseDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItem {
        val binding = CellCourseListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseItem(binding, event)
    }

    override fun onBindViewHolder(holder: CourseItem, position: Int) {
        holder.bind(getItem(position))
    }

    object CourseDiff : DiffUtil.ItemCallback<Pair<Course, Boolean>>() {
        override fun areItemsTheSame(
            oldItem: Pair<Course, Boolean>,
            newItem: Pair<Course, Boolean>
        ): Boolean {
            return oldItem.first.courseId == newItem.first.courseId
        }

        override fun areContentsTheSame(
            oldItem: Pair<Course, Boolean>,
            newItem: Pair<Course, Boolean>
        ): Boolean {
            var a = 0
            if (oldItem.first != newItem.first)
                a = 1
            if (oldItem.second != newItem.second)
                a = 1
            return a == 0
        }

    }

    class CourseItem(
        private val binding: CellCourseListBinding,
        private val event: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { event(bindingAdapterPosition) }
        }

        fun bind(course: Pair<Course, Boolean>) {
            binding.root.isSelected = course.second
            binding.cellCourseListTitle.text = course.first.title
            binding.cellCourseListDay.text = Day.fromNumber(course.first.day).name
            binding.cellCourseListSection.text = course.first.section.toString()
            binding.cellCourseListClassroom.text = course.first.classroom
            binding.cellCourseListTeacher.text = course.first.teacher
            binding.cellCourseListWeek.text = course.first.week.toString()
        }
    }
}