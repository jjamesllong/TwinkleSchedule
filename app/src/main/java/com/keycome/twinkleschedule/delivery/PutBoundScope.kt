package com.keycome.twinkleschedule.delivery

interface PutBoundScope {

    fun putLong(title: String, content: Long)

    fun putInt(title: String, content: Int)

    fun putString(title: String, content: String)
}