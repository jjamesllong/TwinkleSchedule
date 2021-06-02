package com.keycome.twinkleschedule.extension

import android.content.Context
import android.widget.Toast
import com.keycome.twinkleschedule.App

fun toast(
    content: String,
    duration: Int = Toast.LENGTH_SHORT,
    context: Context = App.context
) {
    Toast.makeText(context, content, duration).show()
}

fun toast(
    content: Int,
    duration: Int = Toast.LENGTH_SHORT,
    context: Context = App.context
) {
    Toast.makeText(context, content, duration).show()
}