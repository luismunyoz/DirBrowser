package com.luismunyoz.dirbrowser.app.browser.list

import com.appmattus.kotlinfixture.kotlinFixture
import com.luismunyoz.core.NetworkError
import com.luismunyoz.domain.browser.model.Item
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BrowserListReducerTest {

    private val fixture = kotlinFixture()

    private val reducer = BrowserListReducer()

    @Test
    fun `should update the state when loading`() = runTest {
        val initialState = BrowserListContract.State.initial()
        val updatedState = reducer(
            initialState,
            BrowserListContract.Action.ShowLoading
        )

        updatedState.itemsState shouldBe ItemsState.Loading
    }

    @Test
    fun `should update the state when item loaded`() = runTest {
        val initialState = BrowserListContract.State.initial()
        val theFolder = fixture<Item.Folder>()

        val updatedState = reducer(
            initialState,
            BrowserListContract.Action.ShowFolder(listOf(theFolder))
        )

        updatedState.itemsState shouldBe ItemsState.Loaded(listOf(theFolder))
    }

    @Test
    fun `should update the state to load the empty view`() = runTest {
        val initialState = BrowserListContract.State.initial()

        val updatedState = reducer(
            initialState,
            BrowserListContract.Action.ShowEmptyView
        )

        updatedState.itemsState shouldBe ItemsState.Empty
    }

    @Test
    fun `should update the state when error during user loading`() = runTest {
        val initialState = BrowserListContract.State.initial()
        val theError = NetworkError

        val updatedState = reducer(
            initialState,
            BrowserListContract.Action.ShowError(theError.toString())
        )

        updatedState.itemsState shouldBe ItemsState.Error(theError.toString())
    }

    @Test
    fun `should remove the effect when consumed`() = runTest {
        val initialState = BrowserListContract.State.initial().copy(
            effect = BrowserListContract.State.Effect.NavigateToItem(fixture())
        )

        val updatedState = reducer(
            initialState,
            BrowserListContract.Action.OnEffectConsumed
        )

        updatedState.effect shouldBe BrowserListContract.State.Effect.None
    }

    @Test
    fun `should emit the navigation item if the item is in the state`() = runTest {
        val theFolder = fixture<Item.Folder>()
        val initialState = BrowserListContract.State.initial().copy(
            itemsState = ItemsState.Loaded(listOf(theFolder))
        )

        val updatedState = reducer(
            initialState,
            BrowserListContract.Action.NavigateToItem(theFolder.id)
        )

        updatedState.effect shouldBe BrowserListContract.State.Effect.NavigateToItem(theFolder)
    }

    @Test
    fun `should emit no effect if the item is in the state`() = runTest {
        val initialState = BrowserListContract.State.initial().copy(
            itemsState = ItemsState.Loaded(emptyList())
        )

        val updatedState = reducer(
            initialState,
            BrowserListContract.Action.NavigateToItem("randomId")
        )

        updatedState.effect shouldBe BrowserListContract.State.Effect.None
    }
}