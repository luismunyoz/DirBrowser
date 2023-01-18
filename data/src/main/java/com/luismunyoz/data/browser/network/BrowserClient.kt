package com.luismunyoz.data.browser.network

import com.luismunyoz.data.browser.BrowserApi
import com.luismunyoz.core.Result
import com.luismunyoz.core.map
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import com.luismunyoz.network.util.toResult
import javax.inject.Inject

class BrowserClient @Inject constructor(
    private val api: BrowserApi
) {

    suspend fun getUser(): Result<User> =
        api.getUser()
            .toResult()
            .map {
                it.toDomain()
            }

    suspend fun getItems(itemId: String): Result<List<Item>> =
        api.getItems(itemId)
            .toResult()
            .map { list ->
                list.map { it.toDomain() }
            }
}