package com.luismunyoz.data.browser.di

import com.luismunyoz.data.browser.BrowserApi
import com.luismunyoz.data.browser.BrowserRepository
import com.luismunyoz.domain.browser.BrowserRepositoryContract
import com.luismunyoz.network.di.BaseURLQualifier
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideBrowserApi(
        @BaseURLQualifier baseUrl: String,
        okHttpClient: OkHttpClient
    ): BrowserApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
            .create(BrowserApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRepository(
        browserRepository: BrowserRepository
    ): BrowserRepositoryContract
}