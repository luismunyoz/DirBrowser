package com.luismunyoz.core

fun <T> successOf(value: T) = Result.Success(value)

inline fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(mapper(value))
    is Result.Error -> this
}