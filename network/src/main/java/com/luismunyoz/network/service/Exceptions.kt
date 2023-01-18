package com.luismunyoz.network.service

import com.luismunyoz.core.AuthorizationError
import com.luismunyoz.core.MalformedContractError
import com.luismunyoz.core.Result
import com.luismunyoz.core.UnknownError

data class MalformedContractException(override val cause: Throwable) : Exception(), MappableError {
    override fun toError(): Result.Error = MalformedContractError(cause)
}

object AuthorizationException : Exception(), MappableError {
    override fun toError(): Result.Error = AuthorizationError
}

interface MappableError {

    fun toError(): Result.Error

}

fun Throwable.toError(): Result.Error {
    return if (this is MappableError) {
        this.toError()
    } else {
        UnknownError(this)
    }
}