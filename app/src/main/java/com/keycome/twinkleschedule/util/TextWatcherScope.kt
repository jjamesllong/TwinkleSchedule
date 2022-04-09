package com.keycome.twinkleschedule.util

import android.text.Editable

interface TextWatcherScope {

    fun beforeTextChanged(
        block: (
            sequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) -> Unit
    )

    fun onTextChanged(
        block: (
            sequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) -> Unit
    )

    fun afterTextChanged(block: (sequence: Editable?) -> Unit)
}