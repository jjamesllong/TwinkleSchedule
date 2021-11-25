package com.keycome.twinkleschedule.presentation.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.databinding.ViewCourseEditingInfoHeaderBinding
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.CourseField

class EditCourseHeaderAdapter(
    private val viewModel: RecordViewModel
) : RecyclerView.Adapter<EditCourseHeaderAdapter.HeaderView>() {

    class HeaderView(
        private val viewModel: RecordViewModel,
        private val binding: ViewCourseEditingInfoHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                EditTextDialog(it.context) {
                    onPositiveButtonPressed {
                        viewModel.liveEditingCourse.updateField(
                            CourseField.Title(textContent!!),
                            bindingAdapterPosition
                        )
                    }
                }.show()
            }
        }

        fun onBind(title: String) {
            binding.editingTextTitle.text = title
        }
    }

    private var courseList: List<Course>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderView {
        val b = ViewCourseEditingInfoHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderView(viewModel, b)
    }

    override fun onBindViewHolder(holder: HeaderView, position: Int) {
        courseList?.let {
            holder.onBind(if (it.isNotEmpty()) it[0].title else "")
        }
    }

    override fun getItemCount() = 1

    fun updateList(list: List<Course>) {
        courseList = list
        notifyItemChanged(0)
    }
}