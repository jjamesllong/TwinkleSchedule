package com.keycome.twinkleschedule.util.slidingtrigger

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

@SuppressLint("ClickableViewAccessibility")
class SlidingTrigger(
    private val view: View,
    private val cellList: List<View>,
    private val stateChangeCallback: (size: Int, state: Int) -> Unit
) : View.OnTouchListener {

    private var currentIndex = -1

    init {
        view.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return event?.let { motionEvent ->
            return@let when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    for (i in cellList.indices) {
                        val textView = cellList[i]
                        val x = motionEvent.x.toInt()
                        val y = motionEvent.y.toInt()
                        val xIn = textView.left < x && x < textView.right
                        if (xIn) {
                            val yIn = textView.top < y && y < textView.bottom
                            if (yIn) {
                                currentIndex = i
                                textView.isSelected = !textView.isSelected
                                return@let true
                            }
                        }
                    }
                    false
                }
                MotionEvent.ACTION_MOVE -> {
                    for (i in cellList.indices) {
                        val textView = cellList[i]
                        val x = motionEvent.x.toInt()
                        val y = motionEvent.y.toInt()
                        val xIn = textView.left < x && x < textView.right
                        if (xIn) {
                            val yIn = textView.top < y && y < textView.bottom
                            if (yIn) {
                                if (i != currentIndex) {
                                    currentIndex = i
                                    textView.isSelected = !textView.isSelected
                                }
                                break
                            }
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val state = calculateCellStateNumber()
                    stateChangeCallback(cellList.size, state)
                    true
                }
                else -> false
            }
        } ?: false
    }

    private fun calculateCellStateNumber(): Int {
        var state = 0
        cellList.forEachIndexed { index, textView ->
            if (textView.isSelected) {
                state = state or (1 shl index)
            }
        }
        return state
    }

    private fun callback() {
        val state = calculateCellStateNumber()
        stateChangeCallback(cellList.size, state)
    }

    fun setCellStateNon() {
        cellList.forEach { it.isSelected = false }
        callback()
    }

    fun setCellStateAll() {
        cellList.forEach { it.isSelected = true }
        callback()
    }

    fun setCellSateOdd() {
        cellList.forEachIndexed { index, view ->
            view.isSelected = index % 2 == 0
        }
        callback()
    }

    fun setCellStateEven() {
        cellList.forEachIndexed { index, view ->
            view.isSelected = index % 2 == 1
        }
        callback()
    }

    fun setCellState(state: Int) {
        val times = cellList.size
        var i = 0
        while (i < times) {
            cellList[i].isSelected = (state and (1 shl i)) > 0
            i++
        }
        callback()
    }
}

fun View.toSlidingTrigger(
    cellList: List<View>,
    stateChangeCallback: (size: Int, state: Int) -> Unit
): SlidingTrigger {
    return SlidingTrigger(this, cellList, stateChangeCallback)
}

fun Int.toCellStateWithSize(size: Int): CellState {
    val state = this
    val times = size - 1
    var i = 0
    return if (state == 0) {
        CellState.None
    } else {
        var entireBits = 1
        while (i < times) {
            entireBits = (entireBits shl 1) or 1
            i++
        }
        if (state == entireBits) {
            CellState.All
        } else {
            var singleBits = 1
            i = 0
            while (i < times) {
                singleBits = (singleBits shl 2) or 1
                i += 2
            }
            if (state == singleBits) {
                CellState.Odd
            } else {
                var doubleBits = 0
                i = 0
                while (i < times) {
                    doubleBits = (doubleBits shl 2) or 2
                    i += 2
                }
                if (state == doubleBits) {
                    CellState.Even
                } else {
                    CellState.Other
                }
            }
        }
    }
}