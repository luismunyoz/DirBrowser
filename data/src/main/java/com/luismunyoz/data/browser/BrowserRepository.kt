package com.luismunyoz.data.browser

import com.luismunyoz.domain.browser.BrowserRepositoryContract
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import com.luismunyoz.core.Result
import com.luismunyoz.data.browser.network.BrowserClient
import javax.inject.Inject

class BrowserRepository @Inject constructor(
    private val browserClient: BrowserClient
): BrowserRepositoryContract {

    override suspend fun getFolder(id: String): Result<List<Item>> {
        return browserClient.getItems(id)
    }

    override suspend fun getMe(): Result<User> {
        return browserClient.getUser()
    }
}