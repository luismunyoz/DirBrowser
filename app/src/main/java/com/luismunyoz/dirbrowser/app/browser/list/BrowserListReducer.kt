package com.luismunyoz.dirbrowser.app.browser.list

import com.luismunyoz.dirbrowser.app.browser.UserState
import com.luismunyoz.mvi.framework.Reducer
import javax.inject.Inject

class BrowserListReducer @Inject constructor() :
    Reducer<BrowserListContract.Action, BrowserListContract.State> {

    override suspend fun invoke(
        currentState: BrowserListContract.State,
        action: BrowserListContract.Action
    ): BrowserListContract.State =
        when (action) {
            BrowserListContract.Action.ShowLoading ->
                currentState.copy(itemsState = ItemsState.Loading)
            BrowserListContract.Action.ShowEmptyView ->
                currentState.copy(itemsState = ItemsState.Empty)
            is BrowserListContract.Action.ShowFolder ->
                currentState.copy(itemsState = ItemsState.Loaded(action.items))
            is BrowserListContract.Action.ShowError ->
                currentState.copy(itemsState = ItemsState.Error(action.message))
            is BrowserListContract.Action.NavigateToItem -> {
                currentState.copy(
                    effect = currentState.itemsState.takeIf { it is ItemsState.Loaded }?.let {
                        (it as ItemsState.Loaded).items.firstOrNull { it.id == action.id }
                    }?.let {
                        BrowserListContract.State.Effect.NavigateToItem(it)
                    } ?: BrowserListContract.State.Effect.None
                )
            }
            BrowserListContract.Action.OnEffectConsumed ->
                currentState.copy(effect = BrowserListContract.State.Effect.None)
        }
}