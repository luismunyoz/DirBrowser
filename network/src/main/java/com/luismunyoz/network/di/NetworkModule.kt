package com.luismunyoz.network.di

import com.luismunyoz.network.BuildConfig
import com.luismunyoz.network.interceptors.AuthInterceptor
import com.luismunyoz.network.util.createLoggingInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @BaseURLQualifier
    @Provides
    fun provideBaseURL() = BuildConfig.BASE_URL

    @AuthInterceptorUsername
    @Provides
    fun provideUsername() = BuildConfig.AUTH_LOGIN

    @AuthInterceptorPassword
    @Provides
    fun providePassword() = BuildConfig.AUTH_PASSWORD

    @LoggingInterceptorQualifier
    @Provides
    fun provideLoggingInterceptor(): Interceptor =
        createLoggingInterceptor(BuildConfig.DEBUG)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class InterceptorModule {

    @AuthInterceptorQualifier
    @Binds
    abstract fun bindAuthInterceptor(
        authInterceptor: AuthInterceptor
    ): Interceptor
}