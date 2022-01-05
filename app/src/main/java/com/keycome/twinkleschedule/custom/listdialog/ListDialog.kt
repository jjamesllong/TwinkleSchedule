package com.keycome.twinkleschedule.custom.listdialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.TemplateDialog
import com.keycome.twinkleschedule.record.LayoutSpec

class ListDialog<T>(
    context: Context,
    private val rcAdapter: Adapter<T>,
    block: (ListDialog<T>.() -> Unit)? = null
) : TemplateDialog(context) {
    override val body: View
        get() = RecyclerView(context).apply {
            adapter = rcAdapter
            when (val spec = rcAdapter.layoutSpec) {
                is LayoutSpec.Linear -> {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        160 * rcAdapter.originalList.size
                    )

                    layoutManager =
                        if (spec.orientation == LayoutSpec.vertical)
                            LinearLayoutManager(context)
                        else
                            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
                is LayoutSpec.Grid -> {
                    val p = rcAdapter.originalList.size
                    val b = spec.span
                    val q = p / b
                    val d = if (p % b == 0) q else q + 1
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        requireWidth() / b * d
                    )

                    layoutManager =
                        if (spec.orientation == LayoutSpec.vertical)
                            GridLayoutManager(context, spec.span)
                        else
                            GridLayoutManager(
                                context, spec.span, RecyclerView.HORIZONTAL, false
                            )
                }
            }
        }

    init {
        block?.invoke(this)
    }

    class Holder(frameLayout: FrameLayout) : RecyclerView.ViewHolder(frameLayout) {
        val text: TextView = frameLayout.getChildAt(0) as TextView
    }

    abstract class Adapter<E>(val originalList: List<E>) : RecyclerView.Adapter<Holder>() {

        abstract val layoutSpec: LayoutSpec

        abstract fun converter(element: E): String

        val statusList = originalList.map { false }.toMutableList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(FrameLayout(parent.context).apply {
                when (val spec = layoutSpec) {
                    is LayoutSpec.Grid -> {
                        val p = originalList.size
                        val b = spec.span
                        val q = p / b
                        val d = if (p % b == 0) q else q + 1
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            parent.height / d
                        )
                    }
                    is LayoutSpec.Linear -> {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            160
                        )
                    }
                }
                addView(TextView(parent.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ).also {
                        it.setMargins(15)
                        it.gravity = Gravity.CENTER
                    }
                    gravity = Gravity.CENTER
                    setTextColor(resources.getColor(R.color.gray, null))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                })
            })
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.text.text = converter(originalList[position])
            holder.text.background = requestBackGround("#DFDFE0")
            holder.text.setOnClickListener {
                if (statusList[position])
                    it.background = requestBackGround("#DFDFE0")
                else
                    it.background = requestBackGround("#DFCFE0")
                statusList[position] = !statusList[position]
            }
        }

        override fun getItemCount() = originalList.size

        private fun requestBackGround(fillColor: String) = GradientDrawable().apply {
            val strokeWidth = 5
            val roundRadius = 15f
            val strokeColor = Color.parseColor("#2E3135")
            val backgroundColor = Color.parseColor(fillColor)
            setStroke(strokeWidth, strokeColor)
            cornerRadius = roundRadius
            setColor(backgroundColor)
        }

    }

    fun requestSelectedList(): List<T> {
        val l = mutableListOf<T>()
        rcAdapter.statusList.forEachIndexed { i, s ->
            if (s) l.add(rcAdapter.originalList[i])
        }
        return l
    }
}