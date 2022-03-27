package com.keycome.twinkleschedule.delivery

interface Target<out T> {
    val target: T
}