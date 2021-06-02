package com.keycome.twinkleschedule.custom

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.children

class CustomPopupMenu(
    context: Context, private var linearLayout: LinearLayout? = null
) : PopupWindow(context) {
    private var itemSelectedListener: View.OnClickListener? = null


    init {
        super.setContentView(linearLayout)
        super.setFocusable(true)
        super.setOutsideTouchable(true)
    }

    fun setOnItemSelectedListener(listener: (View) -> Unit) {
        itemSelectedListener = View.OnClickListener {
            super.dismiss()
            listener(it)
        }
        linearLayout?.run {
            for (itemView in children)
                itemView.setOnClickListener(itemSelectedListener)
        }
    }
}