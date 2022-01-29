package com.keycome.twinkleschedule.preference

import com.tencent.mmkv.MMKV

class MMKVPreference : SharedPreference {

    private val instance = MMKV.defaultMMKV()

    override fun encodeShort(key: String, variable: Short) {
        TODO("Not yet implemented")
    }

    override fun decodeShort(key: String, default: Short): Short {
        TODO("Not yet implemented")
    }

    override fun encodeInt(key: String, variable: Int) {
        instance.encode(key, variable)
    }

    override fun decodeInt(key: String, default: Int): Int {
        return instance.decodeInt(key, default)
    }

    override fun encodeLong(key: String, variable: Long) {
        instance.encode(key, variable)
    }

    override fun decodeLong(key: String, default: Long): Long {
        return instance.decodeLong(key, default)
    }

    override fun encodeFloat(key: String, variable: Float) {
        instance.encode(key, variable)
    }

    override fun decodeFloat(key: String, default: Float): Float {
        return instance.decodeFloat(key, default)
    }

    override fun encodeDouble(key: String, variable: Double) {
        instance.encode(key, variable)
    }

    override fun decodeDouble(key: String, default: Double): Double {
        return instance.decodeDouble(key, default)
    }

    override fun encodeChar(key: String, variable: Char) {
        TODO("Not yet implemented")
    }

    override fun decodeChar(key: String, default: Char): Char {
        TODO("Not yet implemented")
    }

    override fun encodeString(key: String, variable: String) {
        instance.encode(key, variable)
    }

    override fun decodeString(key: String, default: String): String {
        return instance.decodeString(key, default) ?: default
    }

    override fun encodeBool(key: String, variable: Boolean) {
        instance.encode(key, variable)
    }

    override fun decodeBool(key: String, default: Boolean): Boolean {
        return instance.decodeBool(key, default)
    }

}