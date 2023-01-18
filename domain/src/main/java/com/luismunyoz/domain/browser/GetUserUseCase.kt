package com.luismunyoz.domain.browser

import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val browserRepositoryContract: BrowserRepositoryContract
) {

    suspend operator fun invoke() = browserRepositoryContract.getUser()
}