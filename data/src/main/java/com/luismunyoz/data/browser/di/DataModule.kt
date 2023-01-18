package com.luismunyoz.data.browser.di

import com.luismunyoz.data.browser.BrowserRepository
import com.luismunyoz.domain.browser.BrowserRepositoryContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRepository(
        browserRepository: BrowserRepository
    ): BrowserRepositoryContract
}