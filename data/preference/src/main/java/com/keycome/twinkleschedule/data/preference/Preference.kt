package com.keycome.twinkleschedule.data.preference

import android.content.Context

interface Preference {

    fun subscribeString(name: String, action: (String) -> Unit)

    fun unsubscribeString(name: String, action: (String) -> Unit)

    fun writeString(name: String, value: String)

    companion object {

        fun getBy(context: Context): Preference {
            return PreferenceImpl(context)
        }
    }

}