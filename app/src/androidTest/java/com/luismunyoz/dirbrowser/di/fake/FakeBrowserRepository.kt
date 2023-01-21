package com.luismunyoz.dirbrowser.di.fake

import com.appmattus.kotlinfixture.kotlinFixture
import com.luismunyoz.core.Result
import com.luismunyoz.core.successOf
import com.luismunyoz.domain.browser.BrowserRepositoryContract
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import javax.inject.Inject

class FakeBrowserRepository @Inject constructor(): BrowserRepositoryContract {

    private val fixture = kotlinFixture()

    override suspend fun getFolder(id: String): Result<List<Item>> {
        return successOf(fixture())
    }

    override suspend fun getUser(): Result<User> {
        return successOf(fixture())
    }
}