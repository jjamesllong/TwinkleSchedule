package com.keycome.twinkleschedule.data.preference

import android.content.Context
import android.util.Log
import com.tencent.mmkv.MMKV

class MMKVAccessImpl(context: Context) : PreferenceAccess {

    private val delegate: MMKV by lazy {
        val rootDir = MMKV.initialize(context.applicationContext)
        Log.d(tag, "initialize MMKV, root directory: $rootDir")
        return@lazy MMKV.defaultMMKV()
    }

    override fun encodeString(name: String, value: String) {
        delegate.encode(name, value)
    }

    override fun decodeString(name: String): String? {
        return delegate.decodeString(name)
    }

    companion object {
        private const val tag = "MMKVAccessImpl"
    }

}