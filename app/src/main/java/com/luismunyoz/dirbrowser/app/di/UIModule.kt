package com.luismunyoz.dirbrowser.app.di

import android.content.Context
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object UIModule {

    @Provides
    fun providePicasso(
        okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ) = Picasso.Builder(context)
        .downloader(OkHttp3Downloader(okHttpClient))
        .memoryCache(LruCache(context))
        .build()
}