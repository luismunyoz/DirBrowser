package com.luismunyoz.dirbrowser.app.browser

import app.cash.turbine.test
import com.appmattus.kotlinfixture.kotlinFixture
import com.luismunyoz.core.NetworkError
import com.luismunyoz.core.successOf
import com.luismunyoz.domain.browser.GetUserUseCase
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BrowserActorTest {

    private val getUserUseCaseMock: GetUserUseCase = mockk()

    private val fixture = kotlinFixture()

    @Test
    fun `should load user on initialized`() = runTest {
        val theUser = getUser()
        coEvery { getUserUseCaseMock.invoke() } returns successOf(theUser)

        val actor = getActor()
        actor(BrowserContract.Event.OnInitialized)
            .test {
                awaitItem() shouldBe BrowserContract.Action.ShowLoading
                awaitItem() shouldBe BrowserContract.Action.ShowUser(theUser)
                awaitComplete()
            }

        coVerify { getUserUseCaseMock.invoke() }
    }

    @Test
    fun `should show error if error getting the user`() = runTest {
        coEvery { getUserUseCaseMock.invoke() } returns NetworkError

        val actor = getActor()
        actor(BrowserContract.Event.OnInitialized)
            .test {
                awaitItem() shouldBe BrowserContract.Action.ShowLoading
                awaitItem() shouldBe BrowserContract.Action.ShowError(NetworkError.toString())
                awaitComplete()
            }

        coVerify { getUserUseCaseMock.invoke() }
    }

    @Test
    fun `should emit action to navigate to folder`() = runTest {
        val theFolder = fixture<Item.Folder>()

        val actor = getActor()
        actor(BrowserContract.Event.OnItemClicked(theFolder))
            .test {
                awaitItem() shouldBe BrowserContract.Action.NavigateToFolder(theFolder)
                awaitComplete()
            }
    }

    @Test
    fun `should emit action to navigate to image`() = runTest {
        val theImage = fixture<Item.Image>()

        val actor = getActor()
        actor(BrowserContract.Event.OnItemClicked(theImage))
            .test {
                awaitItem() shouldBe BrowserContract.Action.NavigateToImage(theImage)
                awaitComplete()
            }
    }

    @Test
    fun `should do nothing if the type is Other`() = runTest {
        val otherItem = fixture<Item.Other>()

        val actor = getActor()
        actor(BrowserContract.Event.OnItemClicked(otherItem))
            .test {
                awaitComplete()
            }
    }

    @Test
    fun `should emit action to consume an effect`() = runTest {
        val actor = getActor()
        actor(BrowserContract.Event.OnEffectConsumed)
            .test {
                awaitItem() shouldBe BrowserContract.Action.OnEffectConsumed
                awaitComplete()
            }
    }

    private fun getActor(): BrowserActor {
        return BrowserActor(
            getUserUseCaseMock
        )
    }

    private fun getUser() = fixture<User>()

}