package com.keycome.twinkleschedule.extension.strings

import com.keycome.twinkleschedule.extension.chars.toNumber

fun String.toIntFromHex(): Int {
    var i = 0
    for (c in this) {
        if (i == 0 && (c == '0' || c == 'x' || c == 'X')) {
            continue
        } else {
            val b = c.toNumber()
            i = i shl 4
            i = i or b
        }
    }
    return i
}