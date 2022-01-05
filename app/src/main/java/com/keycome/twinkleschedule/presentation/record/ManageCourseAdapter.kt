package com.keycome.twinkleschedule.presentation.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellCourseManagerBinding
import com.keycome.twinkleschedule.record.sketch.Course

class ManageCourseAdapter(val fragment: ManageCourseFragment) :
    ListAdapter<Course, ManageCourseAdapter.CourseItem>(CourseDiffCallback) {

    object CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return true
        }
    }

    class CourseItem(
        private val binding: CellCourseManagerBinding,
        private val fragment: ManageCourseFragment
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                with(fragment.optionsLayout) {
                    tag = bindingAdapterPosition
                    visibility = if (isVisible) View.GONE else View.VISIBLE
                }
            }
        }

        fun onBind(course: Course) {
            binding.cellCourseManagerTitle.text = course.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItem {
        val b = CellCourseManagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseItem(b, fragment)
    }

    override fun onBindViewHolder(holder: CourseItem, position: Int) {
        holder.onBind(currentList[position])
    }
}