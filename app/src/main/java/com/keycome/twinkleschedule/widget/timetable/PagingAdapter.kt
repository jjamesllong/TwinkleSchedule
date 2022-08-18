package com.keycome.twinkleschedule.widget.timetable

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.extension.strings.toIntFromHex
import com.keycome.twinkleschedule.extension.toCharacter
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.TimetableDescriber

class PagingAdapter(
    private val l: TimetableView.OnCourseClickListener
) : RecyclerView.Adapter<PagingAdapter.Page>() {

    private var timetableDescriber: TimetableDescriber? = null
    private val weeklyCourseDesign: MutableList<List<CourseDesign>?> = arrayListOf()

    private val pageRow: Int
        get() = timetableDescriber?.schedule?.endSection ?: 0

    private val pageColumn: Int
        get() = timetableDescriber?.schedule?.endDay ?: 0

    private val weekToSectionText = mutableMapOf<Int, List<String>>()

    private val sectionText by lazy {
        timetableDescriber!!.dailyRoutines.map {
            it.routines.mapIndexed { index, section ->
                StringBuilder()
                    .append(index + 1)
                    .append("\n")
                    .append(section.from.formatWithoutSecond24())
                    .toString()
            }
        }
    }

    private val dayText: List<String> by lazy {
        List(timetableDescriber!!.schedule.endDay) {
            Day.fromOrdinal(it).toCharacter()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Page {
        return Page(TimetableView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
            )
            row = pageRow
            column = pageColumn
            val v = (50 * resources.displayMetrics.density + 0.5f).toInt()
            topBarMargin = v
            sideBarMargin = v
            setOnCourseClickListener(l)
        })
    }

    override fun onBindViewHolder(holder: Page, position: Int) {

        val week = position + 1

        if (weeklyCourseDesign[position] == null) {
            val weeklyCourses = timetableDescriber!!.courses.filter { it.week.contains(week) }
            val courseDesign = weeklyCourses.map {
                val id = it.courseId
                val day = it.day.toNumber()
                val sectionStart = it.section.first()
                val sectionEnd = it.section.last()
                val text = StringBuilder()
                    .append(it.title)
                    .append("\n@")
                    .append(it.classroom)
                    .toString()
                val color = it.color.toIntFromHex()
                CourseDesign(id, day, sectionStart, sectionEnd, text, color)
            }
            weeklyCourseDesign[position] = courseDesign
        }
        holder.bind(sectionTextOfWeek(week), dayText, weeklyCourseDesign[position]!!)
    }

    override fun getItemCount(): Int {
        return timetableDescriber?.schedule?.endWeek ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitDescriber(describer: TimetableDescriber) {
        if (timetableDescriber == null) {
            timetableDescriber = describer
            weeklyCourseDesign.clear()
            repeat(describer.schedule.endWeek) { weeklyCourseDesign.add(null) }
            notifyDataSetChanged()
        }
    }

    private fun sectionTextOfWeek(week: Int): List<String> {
        return weekToSectionText[week] ?: putSectionText(week)
    }

    private fun putSectionText(week: Int): List<String> {
        var i = 0
        val oneWeek = 7 * 24 * 60 * 60 * 1000
        val startTime = timetableDescriber?.schedule?.schoolOpeningDate?.toMilliSeconds() ?: 0L
        val weekTime = (week - 1) * oneWeek + startTime
        var before = 0L
        timetableDescriber?.dailyRoutines?.forEachIndexed { index, dailyRoutine ->
            val routineTime = dailyRoutine.startDate.toMilliSeconds()
            if (routineTime in before..weekTime) {
                before = routineTime
                i = index
            }
        }
        return sectionText[i].also { weekToSectionText[week] = it }
    }

    class Page(private val timetable: TimetableView) : RecyclerView.ViewHolder(timetable) {

        fun bind(
            sectionText: List<String>,
            dayText: List<String>,
            weeklyCourseDesign: List<CourseDesign>
        ) {
            timetable.bindSectionBar(sectionText)
            timetable.bindDayBar(dayText)
            timetable.bindCourses(weeklyCourseDesign)
        }
    }
}