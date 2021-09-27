package com.keycome.twinkleschedule.model

sealed interface LayoutSpec {
    companion object {
        const val horizontal = 1
        const val vertical = 2
    }

    data class Linear(val orientation: Int) : LayoutSpec
    data class Grid(val orientation: Int, val span: Int) : LayoutSpec
}