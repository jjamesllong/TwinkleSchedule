package com.keycome.twinkleschedule.extension.chars

fun Char.toNumber(): Int = when (this) {
    '0' -> 0b0000
    '1' -> 0b0001
    '2' -> 0b0010
    '3' -> 0b0011
    '4' -> 0b0100
    '5' -> 0b0101
    '6' -> 0b0110
    '7' -> 0b0111
    '8' -> 0b1000
    '9' -> 0b1001
    'a', 'A' -> 0b1010
    'b', 'B' -> 0b1011
    'c', 'C' -> 0b1100
    'd', 'D' -> 0b1101
    'e', 'E' -> 0b1110
    'f', 'F' -> 0b1111
    else -> throw Exception()
}