package com.luismunyoz.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorUsername

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorPassword

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseURLQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptorQualifier