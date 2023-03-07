package com.keycome.twinkleschedule.data.preference

interface PreferenceAccess {

    fun encodeString(name: String, value: String)

    fun decodeString(name: String): String?
}