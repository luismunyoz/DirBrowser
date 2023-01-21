package com.luismunyoz.dirbrowser.di

import com.luismunyoz.data.browser.BrowserRepository
import com.luismunyoz.data.browser.di.DataModule
import com.luismunyoz.dirbrowser.di.fake.FakeBrowserRepository
import com.luismunyoz.domain.browser.BrowserRepositoryContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
abstract class FakeDataModule {

    @Binds
    abstract fun bindRepository(
        browserRepository: FakeBrowserRepository
    ): BrowserRepositoryContract
}