package com.keycome.twinkleschedule.extension

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

val Context.root: ViewGroup?
    get() = when (this) {
        is Activity -> findViewById(android.R.id.content)
        else -> null
    }