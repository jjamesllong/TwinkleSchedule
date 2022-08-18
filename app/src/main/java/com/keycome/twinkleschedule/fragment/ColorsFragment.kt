package com.keycome.twinkleschedule.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keycome.twinkleschedule.base.BaseFragment

class ColorsFragment : BaseFragment() {

    private var _recyclerView: RecyclerView? = null
    private val recyclerView get() = _recyclerView!!

    private val colorList: IntArray by lazy {
        arguments?.getIntArray(KEY_COLORS) ?: IntArray(size = 0)
    }

    private val action: (Int) -> Unit by lazy {
        (parentFragment as? CourseColorFragment)?.getAction() ?: {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }.also { _recyclerView = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.adapter = ColorsAdapter(colorList, action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recyclerView = null
    }

    companion object {

        const val KEY_COLORS = "colors"

        fun newInstance(colors: IntArray): ColorsFragment {
            return ColorsFragment().apply {
                arguments = Bundle().also { it.putIntArray(KEY_COLORS, colors) }
            }
        }
    }
}

class ColorsAdapter(
    private val colors: IntArray,
    private val action: (Int) -> Unit
) : RecyclerView.Adapter<ColorCell>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorCell {

        val pWidth = (parent.width - parent.paddingStart - parent.paddingEnd).coerceAtLeast(0)
        val width = pWidth / 4

        val itemView = FrameLayout(parent.context).apply {
            layoutParams = GridLayoutManager.LayoutParams(width, width)
            addView(View(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    val margin = (16 * context.resources.displayMetrics.density + 0.5f).toInt()
                    marginStart = margin
                    marginEnd = margin
                    topMargin = margin
                    bottomMargin = margin
                }
            })
        }

        return ColorCell(itemView, action)
    }

    override fun onBindViewHolder(holder: ColorCell, position: Int) {
        holder.bind(colors[position])
    }

    override fun getItemCount(): Int {
        return colors.size
    }
}

class ColorCell(root: FrameLayout, action: (Int) -> Unit) : RecyclerView.ViewHolder(root) {

    private var colorInt = -1

    private val innerBackground = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
    }

    init {
        with(root[0]) {
            background = innerBackground
            setOnClickListener { action(colorInt) }
        }
    }

    fun bind(color: Int) {
        colorInt = color
        innerBackground.setColor(color)
    }
}