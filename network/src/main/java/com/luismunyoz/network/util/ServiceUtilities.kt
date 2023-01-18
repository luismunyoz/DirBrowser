package com.luismunyoz.network.util

import okhttp3.logging.HttpLoggingInterceptor

internal fun createLoggingInterceptor(isLogEnabled: Boolean) = HttpLoggingInterceptor().apply {
    level =
        if (isLogEnabled) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}
