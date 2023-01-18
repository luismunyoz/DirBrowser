package com.luismunyoz.network.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal fun createGsonConverter(): Converter.Factory = GsonConverterFactory.create()

internal fun createHttpClient(httpTimeoutSeconds: Long, networkLogsEnabled: Boolean): OkHttpClient {
    return OkHttpClient.Builder()
        .readTimeout(httpTimeoutSeconds, TimeUnit.SECONDS)
        .connectTimeout(httpTimeoutSeconds, TimeUnit.SECONDS)
        .writeTimeout(httpTimeoutSeconds, TimeUnit.SECONDS)
        .followRedirects(false)
        .addInterceptor(createLoggingInterceptor(networkLogsEnabled))
        .build()
}

internal fun createLoggingInterceptor(isLogEnabled: Boolean) = HttpLoggingInterceptor().apply {
    level = if (isLogEnabled) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}
