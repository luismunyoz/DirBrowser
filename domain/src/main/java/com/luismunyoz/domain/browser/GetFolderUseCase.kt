package com.luismunyoz.domain.browser

import javax.inject.Inject

class GetFolderUseCase @Inject constructor(
    private val browserRepositoryContract: BrowserRepositoryContract
) {

    suspend operator fun invoke(id: String) = browserRepositoryContract.getFolder(id)
}