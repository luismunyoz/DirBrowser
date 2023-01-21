package com.luismunyoz.dirbrowser.app.browser

import com.appmattus.kotlinfixture.kotlinFixture
import com.luismunyoz.core.NetworkError
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BrowserReducerTest {

    private val fixture = kotlinFixture()

    private val reducer = BrowserReducer()

    @Test
    fun `should update the state when loading`() = runTest {
        val initialState = BrowserContract.State.initial()
        val updatedState = reducer(
            initialState,
            BrowserContract.Action.ShowLoading
        )

        updatedState.userState shouldBe UserState.Loading
    }

    @Test
    fun `should update the state when user loaded`() = runTest {
        val initialState = BrowserContract.State.initial()
        val theUser = fixture<User>()

        val updatedState = reducer(
            initialState,
            BrowserContract.Action.ShowUser(theUser)
        )

        updatedState.userState shouldBe UserState.Loaded(theUser)
    }

    @Test
    fun `should update the state when error during user loading`() = runTest {
        val initialState = BrowserContract.State.initial()
        val theError = NetworkError

        val updatedState = reducer(
            initialState,
            BrowserContract.Action.ShowError(theError.toString())
        )

        updatedState.userState shouldBe UserState.Error(theError.toString())
    }

    @Test
    fun `should add the effect to navigate to folder`() = runTest {
        val initialState = BrowserContract.State.initial()
        val theFolder = fixture<Item.Folder>()

        val updatedState = reducer(
            initialState,
            BrowserContract.Action.NavigateToFolder(theFolder)
        )

        updatedState.effect shouldBe BrowserContract.State.Effect.NavigateToFolder(theFolder)
    }

    @Test
    fun `should add the effect to navigate to image`() = runTest {
        val initialState = BrowserContract.State.initial()
        val theImage = fixture<Item.Image>()

        val updatedState = reducer(
            initialState,
            BrowserContract.Action.NavigateToImage(theImage)
        )

        updatedState.effect shouldBe BrowserContract.State.Effect.NavigateToImage(theImage)
    }

    @Test
    fun `should remove the effect when consumed`() = runTest {
        val initialState = BrowserContract.State.initial().copy(
            effect = BrowserContract.State.Effect.NavigateToImage(fixture())
        )

        val updatedState = reducer(
            initialState,
            BrowserContract.Action.OnEffectConsumed
        )

        updatedState.effect shouldBe BrowserContract.State.Effect.None
    }
}