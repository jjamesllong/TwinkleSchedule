package com.keycome.twinkleschedule.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.children

class PopupMenu(
    context: Context, rootView: ViewGroup, itemEvent: PopupMenu.(View) -> Unit
) : PopupWindow(context) {

    init {
        for (v in rootView.children) v.setOnClickListener { itemEvent(this, it) }
        super.setContentView(rootView)
        super.setFocusable(true)
        super.setOutsideTouchable(true)
    }
}