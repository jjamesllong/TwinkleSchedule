package com.keycome.twinkleschedule.delivery

data class Bound<out T>(val direction: String, val title: String, val content: T)