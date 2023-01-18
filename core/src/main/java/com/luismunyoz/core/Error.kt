package com.luismunyoz.core

object NetworkError : Result.Error()

object AuthorizationError : Result.Error()

data class MalformedContractError(val cause: Throwable) : Result.Error()

data class UnknownError(val cause: Throwable?) : Result.Error()

data class ServiceError(
    val message: String,
    val statusCode: Int = -1
): Result.Error()