package com.luismunyoz.domain.browser

import com.luismunyoz.core.successOf
import com.luismunyoz.domain.browser.model.User
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class GetUserUseCaseTest {

    private val repositoryMock: BrowserRepositoryContract = mockk()

    private val useCase = GetUserUseCase(repositoryMock)

    @Test
    fun `when executing the usecase, repository should be called`() = runTest {
        val mockedUser = mockk<User>()
        coEvery { repositoryMock.getUser() } returns successOf(mockedUser)

        val result = useCase.invoke()

        coVerify { repositoryMock.getUser() }

        result shouldBe successOf(mockedUser)
    }
}