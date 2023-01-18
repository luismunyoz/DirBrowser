package com.luismunyoz.data.browser

import com.luismunyoz.domain.browser.BrowserRepositoryContract
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User

class BrowserRepository: BrowserRepositoryContract {

    override suspend fun getFolder(id: String): com.luismunyoz.core.Result<List<Item>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMe(): com.luismunyoz.core.Result<User> {
        TODO("Not yet implemented")
    }
}