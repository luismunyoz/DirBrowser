package com.luismunyoz.network.util

import com.luismunyoz.core.*
import com.luismunyoz.network.service.AuthorizationException
import com.luismunyoz.network.service.MalformedContractException
import com.luismunyoz.network.service.toError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import okio.IOException
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse
import java.net.HttpURLConnection

@Suppress("UNCHECKED_CAST")
suspend fun <R> Call<R>.toResult(): Result<R> = runCatchingFromSuspend {
    val response = awaitResponse()
    when {
        response.isSuccessful -> {
            if (response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                successOf(Unit as R)
            } else {
                successOf(ensureSuccess(response))
            }
        }
        response.isUnauthorized -> throw AuthorizationException
        else -> ServiceError(message = response.message() ?: "", statusCode = response.code())
    }
}
    .fold(
        onSuccess = { it },
        onFailure = { throwable ->
            when (throwable) {
                is IOException -> NetworkError
                is MalformedContractException -> MalformedContractError(throwable)
                else -> throwable.toError()
            }
        },
    )


inline fun <T> runCatchingFromSuspend(block: () -> T): kotlin.Result<T> {
    return runCatching { block() }
        .onFailure { if (it is CancellationException && it !is TimeoutCancellationException) throw it }
}

/**
 * This method handles a successful response.
 *
 * @return <T> The response body converted to the raw object.
 */
fun <T> ensureSuccess(response: Response<T>): T {
    return response.body() ?: throw MalformedContractException(NullPointerException("Null response body"))
}

/**
 * Extension property that checks if the response is unauthorized.
 * The purpose of this extension is to match Retrofit's Response<T>.isSuccessFull method.
 */
private val <T> Response<T>.isUnauthorized: Boolean
    get() = code() == HttpURLConnection.HTTP_UNAUTHORIZED