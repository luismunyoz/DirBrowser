package com.luismunyoz.network.interceptors

import com.luismunyoz.network.di.AuthInterceptorPassword
import com.luismunyoz.network.di.AuthInterceptorUsername
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    @AuthInterceptorUsername user: String,
    @AuthInterceptorPassword pwd: String
) : Interceptor {

    companion object {
        private const val AUTH_HEADER = "Authorization"
    }

    private val credentials = Credentials.basic(user, pwd)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header(AUTH_HEADER, credentials).build()
        return chain.proceed(authenticatedRequest)
    }
}