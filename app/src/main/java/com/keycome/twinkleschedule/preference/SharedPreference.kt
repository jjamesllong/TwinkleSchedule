package com.keycome.twinkleschedule.preference

interface SharedPreference {

    fun encodeShort(key: String, variable: Short)

    fun decodeShort(key: String, default: Short): Short

    fun encodeInt(key: String, variable: Int)

    fun decodeInt(key: String, default: Int): Int

    fun encodeLong(key: String, variable: Long)

    fun decodeLong(key: String, default: Long): Long

    fun encodeFloat(key: String, variable: Float)

    fun decodeFloat(key: String, default: Float): Float

    fun encodeDouble(key: String, variable: Double)

    fun decodeDouble(key: String, default: Double): Double

    fun encodeChar(key: String, variable: Char)

    fun decodeChar(key: String, default: Char): Char

    fun encodeString(key: String, variable: String)

    fun decodeString(key: String, default: String): String

    fun encodeBool(key: String, variable: Boolean)

    fun decodeBool(key: String, default: Boolean): Boolean

}