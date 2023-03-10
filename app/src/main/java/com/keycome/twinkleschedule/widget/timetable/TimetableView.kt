package com.keycome.twinkleschedule.widget.timetable

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline

class TimetableView : ConstraintLayout {

    var topBarSize = 0
    var sideBarSize = 0

    var row = 0
        set(value) {
            val v = value.coerceAtLeast(0)
            if (field != v) {
                field = v
                ensureHorizontalGuideline(v)
            }
        }
        get() = horizontalGuidelines.size

    var column = 0
        set(value) {
            val v = value.coerceAtLeast(0)
            if (field != v) {
                field = v
                ensureVerticalGuideline(v)
            }
        }
        get() = verticalGuidelines.size

    private var horizontalGuidelines: List<Guideline> = emptyList()
    private var verticalGuidelines: List<Guideline> = emptyList()

    private var sectionBar: List<TextView> = emptyList()
    private var dayBar: List<TextView> = emptyList()
    private var courseBlockList: List<TextView> = emptyList()

    private var onCourseClickListener: OnCourseClickListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (verticalGuidelines.isNotEmpty()) {
            val count = verticalGuidelines.size
            val offset = topBarSize
            val gap = (width - offset) / count
            verticalGuidelines.forEachIndexed { index, guideline ->
                guideline.setGuidelineBegin(offset + index * gap)
            }
        }
        if (horizontalGuidelines.isNotEmpty()) {
            val count = horizontalGuidelines.size
            val offset = sideBarSize
            val gap = (height - offset) / count
            horizontalGuidelines.forEachIndexed { index, guideline ->
                guideline.setGuidelineBegin(offset + index * gap)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun retainBaselineOnly() {
        sectionBar.forEach { removeView(it) }
        dayBar.forEach { removeView(it) }
        courseBlockList.forEach { removeView(it) }
        sectionBar = emptyList()
        dayBar = emptyList()
        courseBlockList = emptyList()
    }

    fun bindSectionBar(texts: List<String>) {
        ensureSectionBar(row)
        if (row != texts.size) {
            return
        }
        sectionBar.forEachIndexed { index, textView -> textView.text = texts[index] }
    }

    fun bindDayBar(texts: List<String>) {
        ensureDayBar(column)
        if (column != texts.size) {
            return
        }
        dayBar.forEachIndexed { index, textView -> textView.text = texts[index] }
    }

    fun bindCourses(courses: List<CourseDesign>) {
        courseBlockList.forEach { removeView(it) }
        configureCourses(courses)
    }

    private fun ensureHorizontalGuideline(count: Int) {
        retainBaselineOnly()
        horizontalGuidelines.forEach { removeView(it) }
        configureGuidelines(HORIZONTAL, count)
    }

    private fun ensureVerticalGuideline(count: Int) {
        retainBaselineOnly()
        verticalGuidelines.forEach { removeView(it) }
        configureGuidelines(VERTICAL, count)
    }

    private fun ensureSectionBar(count: Int) {
        if (sectionBar.isEmpty()) {
            configureSectionBar(count)
        }
    }

    private fun ensureDayBar(count: Int) {
        if (dayBar.isEmpty()) {
            configureDayBar(count)
        }
    }

    private fun configureGuidelines(direction: Int, count: Int) {
        (0 until count).map {
            Guideline(context).apply {
                layoutParams = LayoutParams(0, 0).apply {
                    id = View.generateViewId()
                    orientation = when (direction) {
                        HORIZONTAL -> LayoutParams.HORIZONTAL
                        VERTICAL -> LayoutParams.VERTICAL
                        else -> throw IllegalArgumentException()
                    }
                }
            }.also { addView(it) }
        }.also {
            when (direction) {
                HORIZONTAL -> horizontalGuidelines = it
                VERTICAL -> verticalGuidelines = it
            }
        }
    }

    private fun configureSectionBar(count: Int) {
        (0 until count).map { i ->
            TextView(context).apply {
                id = View.generateViewId()
                layoutParams = LayoutParams(0, 0).apply {
                    startToStart = LayoutParams.PARENT_ID
                    endToEnd = verticalGuidelines[0].id
                    topToTop = horizontalGuidelines[i].id
                    bottomToBottom = if (i == count - 1) {
                        LayoutParams.PARENT_ID
                    } else {
                        horizontalGuidelines[i + 1].id
                    }
                }
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
            }.also { addView(it) }
        }.also { sectionBar = it }
    }

    private fun configureDayBar(count: Int) {
        (0 until count).map { i ->
            TextView(context).apply {
                id = View.generateViewId()
                layoutParams = LayoutParams(0, 0).apply {
                    startToStart = verticalGuidelines[i].id
                    endToEnd = if (i == count - 1) {
                        LayoutParams.PARENT_ID
                    } else {
                        verticalGuidelines[i + 1].id
                    }
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = horizontalGuidelines[0].id
                }
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
            }.also { addView(it) }
        }.also { dayBar = it }
    }

    private fun configureCourses(courses: List<CourseDesign>) {
        courses.map { design ->
            val h = design.day
            val t = design.sectionStart
            val b = design.sectionEnd
            val tv = TextView(context).apply {
                val p = 4.dp
                setPadding(p, p, p, p)
                gravity = Gravity.CENTER
            }
            val lp = LayoutParams(0, 0).apply {
                val margin = 4.dp
                marginStart = margin
                marginEnd = margin
                topMargin = margin
                bottomMargin = margin
                startToStart = verticalGuidelines[h - 1].id
                endToEnd = if (h == verticalGuidelines.size) {
                    LayoutParams.PARENT_ID
                } else {
                    verticalGuidelines[h].id
                }
                topToTop = horizontalGuidelines[t - 1].id
                bottomToBottom = if (b == horizontalGuidelines.size) {
                    LayoutParams.PARENT_ID
                } else {
                    horizontalGuidelines[b].id
                }
            }
            val bg = StateListDrawable().apply {
                val radius = 8.dp.toFloat()
                setEnterFadeDuration(50)
                setExitFadeDuration(400)
                addState(
                    intArrayOf(android.R.attr.state_pressed),
                    GradientDrawable().apply {
                        cornerRadius = radius
                        setColor((0x88A9A9A9).toInt())
                    }
                )
                addState(
                    intArrayOf(-android.R.attr.state_pressed),
                    GradientDrawable().apply {
                        cornerRadius = radius
                        setColor(design.color)
                    }
                )
            }
            tv.layoutParams = lp
            tv.background = bg
            tv.setOnClickListener { onCourseClickListener?.onCourseClick(design.id) }
            tv.text = design.text
            tv.setTextColor(design.textColor)
            addView(tv)
            return@map tv
        }.also { courseBlockList = it }
    }

    fun setOnCourseClickListener(l: OnCourseClickListener) {
        onCourseClickListener = l
    }

    fun interface OnCourseClickListener {
        fun onCourseClick(id: Long)
    }

    private val Int.dp: Int get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    companion object {

        const val HORIZONTAL = 1
        const val VERTICAL = 2

    }
}