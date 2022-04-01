package com.keycome.twinkleschedule.custom.courseschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellTableViewBinding
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.CourseSchedule

class PagerAdapter : ListAdapter<CourseSchedule, PagerAdapter.TableView>(CourseScheduleDiff) {

    object CourseScheduleDiff : DiffUtil.ItemCallback<CourseSchedule>() {
        override fun areItemsTheSame(oldItem: CourseSchedule, newItem: CourseSchedule): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CourseSchedule, newItem: CourseSchedule): Boolean {
            return oldItem.schedule.scheduleId == newItem.schedule.scheduleId
        }

    }

    class TableView(
        binding: CellTableViewBinding,
        pagerAdapter: PagerAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        val dayTable = binding.dayRecyclerView.toDayTable()

        val sectionTable = binding.sectionRecyclerView.toSectionTable()

        val courseTable = binding.courseRecyclerView.toCourseTable().also {
            it.pagerAdapter = pagerAdapter
        }

    }

    private var courseViewClickListener: ((Course) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableView {
        val binding = CellTableViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TableView(binding, this)
    }

    override fun onBindViewHolder(holder: TableView, position: Int) {
        val list = currentList
        holder.dayTable.submitData(list[position].schedule)
        holder.sectionTable.submitData(list[position].schedule)
        holder.courseTable.submitData(list[position])
    }

    fun registerCourseViewClickListener(action: (course: Course) -> Unit) {
        courseViewClickListener = action
    }

    fun onCourseViewClicked(course: Course) {
        courseViewClickListener?.invoke(course)
    }
}