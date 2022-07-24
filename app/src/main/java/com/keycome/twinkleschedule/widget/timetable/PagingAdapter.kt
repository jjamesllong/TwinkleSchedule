package com.keycome.twinkleschedule.widget.timetable

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.extension.toCharacter
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.TimetableDescriber

class PagingAdapter(
    private val l: TimetableView.OnCourseClickListener
) : RecyclerView.Adapter<PagingAdapter.Page>() {

    private var timetableDescriber: TimetableDescriber? = null
    private val weeklyCourseBlock: MutableList<List<CourseBlock>?> = arrayListOf()

    val sectionText by lazy {
        timetableDescriber!!.dailyRoutines[0].routines.mapIndexed { index, section ->
            StringBuilder()
                .append(index + 1)
                .append("\n")
                .append(section.from.formatWithoutSecond24())
                .toString()
        }
    }

    val dayText: List<String> by lazy {
        List(timetableDescriber!!.schedule.endDay) {
            Day.fromOrdinal(it).toCharacter()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Page {
        return Page(TimetableView(parent.context).apply {
            sectionBarWidth = 160
            dayBarHeight = 160
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
            )
            setOnCourseClickListener(l)
        })
    }

    override fun onBindViewHolder(holder: Page, position: Int) {
        if (weeklyCourseBlock[position] == null) {
            val week = position + 1
            val weeklyCourses = timetableDescriber!!.courses.filter { it.week.contains(week) }
            val courseBlock = weeklyCourses.map {
                val id = it.courseId
                val tc = StringBuilder()
                    .append(it.title)
                    .append("\n@")
                    .append(it.classroom)
                    .toString()
                val x = it.section.first()
                val y = it.day.toNumber()
                val z = it.section.size
                CourseBlock(id, tc, x, y, z)
            }
            weeklyCourseBlock[position] = courseBlock
        }
        holder.bind(sectionText, dayText, weeklyCourseBlock[position]!!)
    }

    override fun getItemCount(): Int {
        return timetableDescriber?.schedule?.endWeek ?: 0
    }

    fun submitDescriber(describer: TimetableDescriber) {
        if (timetableDescriber == null) {
            timetableDescriber = describer
            weeklyCourseBlock.clear()
            repeat(describer.schedule.endWeek) { weeklyCourseBlock.add(null) }
            notifyDataSetChanged()
        }
    }

    class Page(private val timetable: TimetableView) : RecyclerView.ViewHolder(timetable) {

        fun bind(
            sectionText: List<String>,
            dayText: List<String>,
            weeklyCourseBlock: List<CourseBlock>
        ) {
            timetable.row = sectionText.size
            timetable.column = dayText.size
            timetable.bindSectionBar(sectionText)
            timetable.bindDayBar(dayText)
            timetable.bindCourses(weeklyCourseBlock)
        }
    }
}