package com.luismunyoz.dirbrowser.di

import com.luismunyoz.network.di.AuthInterceptorPassword
import com.luismunyoz.network.di.AuthInterceptorUsername
import com.luismunyoz.network.di.BaseURLQualifier
import com.luismunyoz.network.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object FakeNetworkModule {

    @BaseURLQualifier
    @Provides
    fun provideBaseURL() = "baseUrl"

    @AuthInterceptorUsername
    @Provides
    fun provideUsername() = "username"

    @AuthInterceptorPassword
    @Provides
    fun providePassword() = "password"

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}