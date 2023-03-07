package com.keycome.twinkleschedule.data.preference

import android.content.Context

class PreferenceImpl(context: Context) : Preference {

    private val access: PreferenceAccess = MMKVAccessImpl(context)

    private val strings: MutableSet<PreferenceWrapper<String>> = mutableSetOf()

    override fun subscribeString(name: String, action: (String) -> Unit) {
        val preferenceWrapper = strings.find {
            it.owner == name
        } ?: PreferenceWrapper<String>(name).also {
            strings.add(it)
        }
        if (preferenceWrapper.add(action)) {
            preferenceWrapper.getValue()?.also(action) ?: access.decodeString(name)?.also {
                preferenceWrapper.setValue(it)
            } ?: "".also {
                preferenceWrapper.setValue(it)
            }
        }
    }

    override fun unsubscribeString(name: String, action: (String) -> Unit) {
        strings.find { it.owner == name }?.remove(action)
    }

    override fun writeString(name: String, value: String) {
        access.encodeString(name, value)
        strings.find { it.owner == name }?.setValue(value)
    }
}