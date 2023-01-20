package com.luismunyoz.domain.browser

import com.luismunyoz.core.successOf
import com.luismunyoz.domain.browser.model.Item
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


internal class GetFolderUseCaseTest {

    private val repositoryMock: BrowserRepositoryContract = mockk()

    private val useCase = GetFolderUseCase(repositoryMock)

    @Test
    fun `when executing the usecase, repository should be called`() = runTest {
        val mockedList = mockk<List<Item>>()
        coEvery { repositoryMock.getFolder(any()) } returns successOf(mockedList)

        val result = useCase.invoke("id")

        coVerify { repositoryMock.getFolder("id") }

        result shouldBe successOf(mockedList)
    }
}