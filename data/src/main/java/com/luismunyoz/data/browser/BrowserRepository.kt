package com.luismunyoz.data.browser

import com.luismunyoz.domain.browser.BrowserRepositoryContract
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import com.luismunyoz.core.Result
import com.luismunyoz.core.onSuccess
import com.luismunyoz.core.successOf
import com.luismunyoz.data.browser.memory_cache.MemoryCache
import com.luismunyoz.data.browser.network.BrowserClient
import javax.inject.Inject

class BrowserRepository @Inject constructor(
    private val browserClient: BrowserClient,
    private val cache: MemoryCache
) : BrowserRepositoryContract {

    override suspend fun getFolder(id: String): Result<List<Item>> {
        return cache.getItems(id)?.let { successOf(it) } ?: browserClient.getItems(id)
            .onSuccess { apiItems ->
                cache.store(id, apiItems)
            }
    }

    override suspend fun getUser(): Result<User> {
        return browserClient.getUser()
    }
}