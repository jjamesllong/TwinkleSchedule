package com.keycome.twinkleschedule.share

data class Mark<T>(
    val target: Target<T>?,
    val initialized: Boolean,
    val reference: Int
)
