package com.keycome.twinkleschedule.extension

import android.content.Context
import android.widget.Toast
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.record.horizon.Day

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

fun dp2px(context: Context, dpValue: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dp(context: Context, pxValue: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Day.toCharacter() = when (this) {
    Day.Monday -> "一"
    Day.Tuesday -> "二"
    Day.Wednesday -> "三"
    Day.Thursday -> "四"
    Day.Friday -> "五"
    Day.Saturday -> "六"
    Day.Sunday -> "日"
}