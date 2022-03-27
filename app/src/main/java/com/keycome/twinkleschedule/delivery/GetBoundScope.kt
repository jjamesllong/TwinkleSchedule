package com.keycome.twinkleschedule.delivery

interface GetBoundScope {

    fun getLong(title: String, default: Long): Long

    fun getInt(title: String, default: Int): Int

    fun getString(title: String, default: String): String
}