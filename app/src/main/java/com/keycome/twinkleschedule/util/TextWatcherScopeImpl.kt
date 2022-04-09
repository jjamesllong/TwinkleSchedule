package com.keycome.twinkleschedule.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TextWatcherScopeImpl(editText: EditText) : TextWatcherScope {

    private var beforeTextChangedCallback: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var onTextChangedCallback: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var afterTextChangedCallback: ((Editable?) -> Unit)? = null

    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                beforeTextChangedCallback?.invoke(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChangedCallback?.invoke(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                afterTextChangedCallback?.invoke(s)
            }

        })
    }

    override fun beforeTextChanged(
        block: (sequence: CharSequence?, start: Int, before: Int, count: Int) -> Unit
    ) {
        beforeTextChangedCallback = block
    }

    override fun onTextChanged(
        block: (sequence: CharSequence?, start: Int, before: Int, count: Int) -> Unit
    ) {
        onTextChangedCallback = block
    }

    override fun afterTextChanged(block: (sequence: Editable?) -> Unit) {
        afterTextChangedCallback = block
    }
}