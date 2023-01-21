package com.luismunyoz.dirbrowser.app.browser.list

import app.cash.turbine.test
import com.appmattus.kotlinfixture.kotlinFixture
import com.luismunyoz.core.NetworkError
import com.luismunyoz.core.successOf
import com.luismunyoz.domain.browser.GetFolderUseCase
import com.luismunyoz.domain.browser.model.Item
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BrowserListActorTest {

    private val getFolderUseCaseMock: GetFolderUseCase = mockk()

    private val fixture = kotlinFixture()

    @Test
    fun `should load folder on initialized if not empty`() = runTest {
        val theFolder = getFolderFixture()
        coEvery { getFolderUseCaseMock.invoke(any()) } returns successOf(listOf(theFolder))

        val actor = getActor()
        actor(BrowserListContract.Event.OnInitialized("id"))
            .test {
                awaitItem() shouldBe BrowserListContract.Action.ShowLoading
                awaitItem() shouldBe BrowserListContract.Action.ShowFolder(listOf(theFolder))
                awaitComplete()
            }

        coVerify { getFolderUseCaseMock.invoke("id") }
    }

    @Test
    fun `should show empty view on initialized if empty`() = runTest {
        coEvery { getFolderUseCaseMock.invoke(any()) } returns successOf(emptyList())

        val actor = getActor()
        actor(BrowserListContract.Event.OnInitialized("id"))
            .test {
                awaitItem() shouldBe BrowserListContract.Action.ShowLoading
                awaitItem() shouldBe BrowserListContract.Action.ShowEmptyView
                awaitComplete()
            }

        coVerify { getFolderUseCaseMock.invoke("id") }
    }

    @Test
    fun `should show error if error getting the folder`() = runTest {
        coEvery { getFolderUseCaseMock.invoke(any()) } returns NetworkError

        val actor = getActor()
        actor(BrowserListContract.Event.OnInitialized("id"))
            .test {
                awaitItem() shouldBe BrowserListContract.Action.ShowLoading
                awaitItem() shouldBe BrowserListContract.Action.ShowError(NetworkError.toString())
                awaitComplete()
            }

        coVerify { getFolderUseCaseMock.invoke("id") }
    }

    @Test
    fun `should emit action to consume an effect`() = runTest {
        val actor = getActor()
        actor(BrowserListContract.Event.OnEffectConsumed)
            .test {
                awaitItem() shouldBe BrowserListContract.Action.OnEffectConsumed
                awaitComplete()
            }
    }

    @Test
    fun `should emit action to navigate to item`() = runTest {
        val actor = getActor()
        actor(BrowserListContract.Event.OnItemClicked("id"))
            .test {
                awaitItem() shouldBe BrowserListContract.Action.NavigateToItem("id")
                awaitComplete()
            }
    }

    private fun getActor(): BrowserListActor {
        return BrowserListActor(
            getFolderUseCaseMock
        )
    }


    private fun getFolderFixture() = fixture<Item.Folder>()
}