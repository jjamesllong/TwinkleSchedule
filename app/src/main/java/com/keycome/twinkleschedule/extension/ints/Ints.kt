package com.keycome.twinkleschedule.extension.ints

infix fun Int.storeWith(other: Int): Int {
    var x = this
    x = x shl 16
    x = x or other
    return x
}

fun Int.restoreToPair(): Pair<Int, Int> {
    val first = (this and (0xFFFF0000).toInt()) ushr 16
    val second = this and 0x0000FFFF
    return Pair(first, second)
}

fun Int.toCharHex(): Char {
    return when (this) {
        0b0000 -> '0'
        0b0001 -> '1'
        0b0010 -> '2'
        0b0011 -> '3'
        0b0100 -> '4'
        0b0101 -> '5'
        0b0110 -> '6'
        0b0111 -> '7'
        0b1000 -> '8'
        0b1001 -> '9'
        0b1010 -> 'A'
        0b1011 -> 'B'
        0b1100 -> 'C'
        0b1101 -> 'D'
        0b1110 -> 'E'
        0b1111 -> 'F'
        else -> throw Exception()
    }
}

fun Int.toStringHex(prefix: String? = null): String {
    val string = StringBuilder()
    val int = this
    for (i in 0 until 32 step 4) {
        val b = 0xF shl i
        val c = (int and b) ushr i
        string.append(c.toCharHex())
    }
    string.reverse()
    prefix?.also { string.insert(0, it) }
    return string.toString()
}