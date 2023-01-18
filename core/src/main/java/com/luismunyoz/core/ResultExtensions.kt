package com.luismunyoz.core

fun <T> successOf(value: T) = Result.Success(value)

inline fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(mapper(value))
    is Result.Error -> this
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> = when (this) {
    is Result.Success -> apply { action(value) }
    is Result.Error -> this
}

inline fun <T> Result<T>.onError(action: (Result.Error) -> Unit): Result<T> = when (this) {
    is Result.Success -> this
    is Result.Error -> apply { action(this) }
}
