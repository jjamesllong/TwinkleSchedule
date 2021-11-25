package com.keycome.twinkleschedule.presentation.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.custom.listdialog.ListDialog
import com.keycome.twinkleschedule.databinding.CellCourseEditingInfoBinding
import com.keycome.twinkleschedule.model.LayoutSpec
import com.keycome.twinkleschedule.model.horizon.Day
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.CourseField

class EditCourseAdapter(private val viewModel: RecordViewModel) :
    ListAdapter<Course, EditCourseAdapter.EditingInfoHolder>(EditingInfoDiff) {

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
                binding.cellCourseEditingInfoDeleteButton ->
                    viewModel.liveEditingCourse.deleteCourse(bindingAdapterPosition)
                binding.editingInfoWeek -> ListDialog(
                    v.context,
                    object : ListDialog.Adapter<Int>((1..24).toList()) {
                        override val layoutSpec: LayoutSpec
                            get() = LayoutSpec.Grid(LayoutSpec.vertical, 5)

                        override fun converter(element: Int): String {
                            return element.toString()
                        }
                    }
                ) {
                    onPositiveButtonPressed {
                        viewModel.liveEditingCourse.updateField(
                            CourseField.Week(requestSelectedList()),
                            bindingAdapterPosition
                        )
                    }
                }.show()
                binding.editingInfoDay -> ListDialog(
                    v.context,
                    object : ListDialog.Adapter<Day>(Day.values().toList()) {
                        override val layoutSpec: LayoutSpec
                            get() = LayoutSpec.Linear(LayoutSpec.vertical)

                        override fun converter(element: Day): String {
                            return element.name
                        }
                    }
                ) {
                    onPositiveButtonPressed {
                        viewModel.liveEditingCourse.updateField(
                            CourseField.FDay(requestSelectedList()[0]),
                            bindingAdapterPosition
                        )
                    }
                }.show()
                binding.editingInfoSection -> ListDialog(
                    v.context,
                    object : ListDialog.Adapter<Int>((1..14).toList()) {
                        override val layoutSpec: LayoutSpec
                            get() = LayoutSpec.Grid(LayoutSpec.vertical, 5)

                        override fun converter(element: Int): String {
                            return element.toString()
                        }
                    }
                ) {
                    onPositiveButtonPressed {
                        viewModel.liveEditingCourse.updateField(
                            CourseField.Section(requestSelectedList()),
                            bindingAdapterPosition
                        )
                    }
                }.show()
                binding.editingInfoTeacher -> EditTextDialog(v.context) {
                    onPositiveButtonPressed {
                        viewModel.liveEditingCourse.updateField(
                            CourseField.Teacher(textContent!!),
                            bindingAdapterPosition
                        )
                    }
                }.show()
                binding.editingInfoClassroom -> EditTextDialog(v.context) {
                    onPositiveButtonPressed {
                        viewModel.liveEditingCourse.updateField(
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