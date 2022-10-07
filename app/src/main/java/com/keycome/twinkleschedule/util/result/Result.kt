package com.keycome.twinkleschedule.util.result

sealed interface Result<T>

class Ok<T>(val content: T) : Result<T>

@JvmInline
value class Err<T>(val message: String) : Result<T>