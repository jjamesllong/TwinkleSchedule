package com.keycome.twinkleschedule.widget.timetable

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.extension.strings.toIntFromHex
import com.keycome.twinkleschedule.extension.toCharacter
import com.keycome.twinkleschedule.record.interval.Date
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

    private val dayText: List<String> by lazy {
        val size = timetableDescriber?.schedule?.endDay ?: 0
        return@lazy List(size) {
            requestDayText(it)
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
            topBarSize = v
            sideBarSize = v
            setOnCourseClickListener(l)
        })
    }

    override fun onBindViewHolder(holder: Page, position: Int) {

        val week = position + 1

        if (weeklyCourseDesign[position] == null) {
            val weeklyCourses = timetableDescriber?.courses?.filter {
                it.week.contains(week)
            } ?: emptyList()
            val courseDesign = weeklyCourses.map {
                val id = it.courseId
                val day = it.day
                val sectionStart = it.section.first()
                val sectionEnd = it.section.last()
                val text = StringBuilder()
                    .append(it.title)
                    .append("\n@")
                    .append(it.classroom)
                    .toString()
                val color = it.color.toIntFromHex()
                val textColor = it.textColor.toIntFromHex()
                return@map CourseDesign(id, day, sectionStart, sectionEnd, text, color, textColor)
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
        return weekToSectionText[week] ?: requestSectionText(week)
    }

    private fun requestSectionText(week: Int): List<String> {
        var i = 0
        val oneWeek = 7 * 24 * 60 * 60 * 1000
        val startTime = Date.fromString(timetableDescriber!!.schedule.startDate).toMilliSeconds()
        val weekTime = (week - 1) * oneWeek + startTime
        var before = 0L
        timetableDescriber!!.routines.forEachIndexed { index, dailyRoutine ->
            val routineTime = Date.fromString(dailyRoutine.startDate).toMilliSeconds()
            if (routineTime in before..weekTime) {
                before = routineTime
                i = index
            }
        }
        val routine = timetableDescriber!!.routines[i]
        return routine.sectionList.map {
            requestSectionText(it)
        }.also { weekToSectionText[i] = it }
    }

    private fun requestDayText(ordinal: Int): String {
        return Day.fromOrdinal(ordinal).toCharacter()
    }

    private fun requestSectionText(section: String): String {
        val s = section.split(delimiters = arrayOf("~", "@"))
        return StringBuilder()
            .append(s[2].toInt())
            .append("\n")
            .append(s[0])
            .toString()
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