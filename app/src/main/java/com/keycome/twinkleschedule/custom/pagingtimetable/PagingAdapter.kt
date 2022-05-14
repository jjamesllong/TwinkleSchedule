package com.keycome.twinkleschedule.custom.pagingtimetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.databinding.CellTableViewBinding
import com.keycome.twinkleschedule.record.timetable.Course
import com.keycome.twinkleschedule.record.timetable.Schedule
import com.keycome.twinkleschedule.record.timetable.Section

class PagingAdapter(
    private val courseSelectedListener: (Course) -> Unit
) : ListAdapter<List<Course>?, PagingAdapter.TimeTableView>(PagerDiff) {

    private var _schedule: Schedule? = null
    private var _sectionList: List<Section>? = null
    private var _pagingCourseList: List<List<Course>?>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableView {
        val binding = CellTableViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeTableView(binding, courseSelectedListener)
    }

    override fun onBindViewHolder(holder: TimeTableView, position: Int) {
        _pagingCourseList?.let {
            it[position]?.let { courseList ->
                holder.onBind(_schedule!!, _sectionList!!, courseList)
            }
        }
    }

    object PagerDiff : DiffUtil.ItemCallback<List<Course>?>() {
        override fun areItemsTheSame(oldItem: List<Course>, newItem: List<Course>): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: List<Course>, newItem: List<Course>): Boolean {
            return false
        }
    }

    class TimeTableView(
        binding: CellTableViewBinding,
        courseSelectedListener: (Course) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dayBar = binding.dayBar.toDayBar()
        private val sectionBar = binding.sectionBar.toSectionBar()
        private val timetableBody = binding.timetableBody.toTimetableBody(courseSelectedListener)

        fun onBind(schedule: Schedule, sectionList: List<Section>, courseList: List<Course>) {
            dayBar.refreshDayNumber(schedule.endDay)
            sectionBar.refreshSectionList(sectionList)
            timetableBody.submitData(schedule, courseList)
        }
    }

    fun submitSchedule(schedule: Schedule) {
        _schedule = schedule
        considerSubmission()
    }

    fun submitSectionList(list: List<Section>) {
        _sectionList = list
        considerSubmission()
    }

    fun submitCourseList(list: List<List<Course>?>) {
        _pagingCourseList = list
        considerSubmission()
    }

    private fun considerSubmission() {
        if (_schedule != null && _sectionList != null && _pagingCourseList != null)
            submitList(_pagingCourseList)
    }
}