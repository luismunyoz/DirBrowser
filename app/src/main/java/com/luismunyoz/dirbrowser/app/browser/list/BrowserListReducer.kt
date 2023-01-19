package com.luismunyoz.dirbrowser.app.browser.list

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
        }
}