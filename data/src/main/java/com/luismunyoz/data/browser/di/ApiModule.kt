package com.luismunyoz.data.browser.di

import com.luismunyoz.data.browser.BrowserApi
import com.luismunyoz.network.di.BaseURLQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BrowserApi::class.java)
    }
}