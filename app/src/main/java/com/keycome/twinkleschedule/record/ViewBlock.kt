package com.keycome.twinkleschedule.record

data class ViewBlock(
    val isCourse: Boolean,
    val spanSize: Int,
    val courseIndex: Int = -1
)