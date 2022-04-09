package com.keycome.twinkleschedule.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class SlidingTriggerListener(
    private val childrenList: List<TextView>,
    private val actionUpCallback: (Int) -> Unit = {}
) : View.OnTouchListener {

    private val row = 4
    private val column = 6
    private var currentIndex = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                var xIndex = -1
                var yIndex = -1
                val x = event.x.toInt()
                val y = event.y.toInt()
                for (i in 0 until row) {
                    val textView = childrenList[i]
                    if (textView.left < x && x < textView.right) {
                        xIndex = i
                        break
                    }
                }
                if (xIndex != -1) {
                    for (j in 0 until column) {
                        val textView = childrenList[j * row]
                        if (textView.top < y && y < textView.bottom) {
                            yIndex = j
                            break
                        }
                    }
                }
                if (yIndex != -1) {
                    currentIndex = yIndex * row + xIndex
                    val textView = childrenList[currentIndex]
                    textView.isSelected = !textView.isSelected
                    return true
                }
                false
            }
            MotionEvent.ACTION_MOVE -> {
                var xIndex = -1
                var yIndex = -1
                val x = event.x.toInt()
                val y = event.y.toInt()
                for (i in 0 until row) {
                    val textView = childrenList[i]
                    if (textView.left < x && x < textView.right) {
                        xIndex = i
                        break
                    }
                }
                if (xIndex != -1) {
                    for (j in 0 until column) {
                        val textView = childrenList[j * row]
                        if (textView.top < y && y < textView.bottom) {
                            yIndex = j
                            break
                        }
                    }
                }
                if (yIndex != -1) {
                    val finalIndex = yIndex * row + xIndex
                    if (finalIndex != currentIndex) {
                        currentIndex = yIndex * row + xIndex
                        val textView = childrenList[currentIndex]
                        textView.isSelected = !textView.isSelected
                    }
                }
                true
            }
            MotionEvent.ACTION_UP -> {
                var allEvenSelected = true
                var allOddSelected = true
                var nonEvenSelected = true
                var nonOddSelected = true
                childrenList.forEachIndexed { index, textView ->
                    val select = textView.isSelected
                    if (index % 2 == 0) {
                        if (allOddSelected) {
                            allOddSelected = select
                        }
                        if (nonOddSelected) {
                            nonOddSelected = !select
                        }
                    } else {
                        if (allEvenSelected) {
                            allEvenSelected = select
                        }
                        if (nonEvenSelected) {
                            nonEvenSelected = !select
                        }
                    }
                }
                val selectAll = allEvenSelected && allOddSelected
                val selectEven = allEvenSelected && nonOddSelected
                val selectOdd = nonEvenSelected && allOddSelected
                val selectNon = nonEvenSelected && nonOddSelected
                val resultCode = when {
                    selectAll -> 1
                    selectEven -> 2
                    selectOdd -> 3
                    selectNon -> 4
                    else -> 0
                }
                actionUpCallback(resultCode)
                true
            }
            else -> false
        }
    }
}