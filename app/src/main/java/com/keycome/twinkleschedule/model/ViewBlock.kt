package com.keycome.twinkleschedule.model

data class ViewBlock(
    val isCourse: Boolean,
    val spanSize: Int,
    val courseIndex: Int = -1
)