package com.keycome.twinkleschedule.presentation.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.databinding.CellCourseEditingInfoBinding
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.CourseField

class AddCourseAdapter(val viewModel: RecordViewModel) :
    ListAdapter<Course, AddCourseAdapter.EditingInfoHolder>(EditingInfoDiff) {

    object EditingInfoDiff : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }

    }

    class EditingInfoHolder(
        val viewModel: RecordViewModel,
        val binding: CellCourseEditingInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val itemEvent: (View) -> Unit = { v ->
            when (v) {
                binding.editingInfoClassroom -> EditTextDialog(v.context) {
                    onPositiveButtonPressed {
                        viewModel.liveCourseList.updateField(
                            CourseField.Classroom(textContent!!),
                            bindingAdapterPosition
                        )
                    }
                }.show()
            }
        }

        init {
            binding.root.forEach { it.setOnClickListener(itemEvent) }
        }

        fun onBindData(course: Course, position: Int) {
            binding.editingTextWeek.text = course.week.toString()
            binding.editingTextDay.text = course.day.toString()
            binding.editingTextSection.text = course.section.toString()
            binding.editingTextTeacher.text = course.teacher
            binding.editingTextClassroom.text = course.classroom
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditingInfoHolder {
        val b = CellCourseEditingInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditingInfoHolder(viewModel, b)
    }

    override fun onBindViewHolder(holder: EditingInfoHolder, position: Int) {
        holder.onBindData(currentList[position], position)
    }

    override fun submitList(list: MutableList<Course>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

}