package com.luismunyoz.data.browser

import com.luismunyoz.core.NetworkError
import com.luismunyoz.core.successOf
import com.luismunyoz.data.browser.memory_cache.MemoryCache
import com.luismunyoz.data.browser.network.BrowserClient
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BrowserRepositoryTest {

    private val memoryCacheMock: MemoryCache = mockk()
    private val browserClientMock: BrowserClient = mockk()

    private val repository = BrowserRepository(browserClientMock, memoryCacheMock)

    @BeforeEach
    fun before() {
    }

    @Nested
    @DisplayName("Request User")
    inner class RequestUser {

        @Test
        fun `should return result from the API`() = runTest {
            val mockedUser = mockk<User>()
            coEvery { browserClientMock.getUser() } returns successOf(mockedUser)

            val result = repository.getUser()

            coVerify { browserClientMock.getUser() }

            result shouldBe successOf(mockedUser)
        }
    }

    @Nested
    @DisplayName("Load Items - Empty cache")
    inner class EmptyCache {

        @BeforeEach
        fun before() {
            every { memoryCacheMock.getItems(any()) } returns null
        }

        @Test
        fun `given successful response, it should return api response and update cache`() =
            runTest {
                val mockedList = mockk<List<Item>>()
                coEvery { browserClientMock.getItems(any()) } returns successOf(mockedList)

                val result = repository.getFolder("id")

                verify {
                    memoryCacheMock.store("id", mockedList)
                }

                result shouldBe successOf(mockedList)
            }

        @Test
        fun `given failing response, it should return api response and not update cache`() =
            runTest {
                coEvery { browserClientMock.getItems(any()) } returns NetworkError

                val result = repository.getFolder("id")

                verify(exactly = 0) {
                    memoryCacheMock.store(any(), any())
                }

                result shouldBe NetworkError
            }
    }

    @Nested
    @DisplayName("Load Items - Populated cache")
    inner class PopulatedCache {

        @Test
        fun `it should return the value stored in the cache`() = runTest {
            val mockedList = mockk<List<Item>>()
            every { memoryCacheMock.getItems(any()) } returns mockedList

            val result = repository.getFolder("id")

            verify(exactly = 0) {
                memoryCacheMock.store(any(), any())
            }
            coVerify(exactly = 0) {
                browserClientMock.getItems(any())
            }

            result shouldBe successOf(mockedList)
        }
    }

}