package com.luismunyoz.domain.browser

import com.luismunyoz.core.Result
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User

interface BrowserRepositoryContract {

    suspend fun getUser(): Result<User>

    suspend fun getFolder(id: String): Result<List<Item>>
}