package com.luismunyoz.dirbrowser.app.browser

import com.luismunyoz.mvi.framework.Reducer
import javax.inject.Inject

class BrowserReducer @Inject constructor() :
    Reducer<BrowserContract.Action, BrowserContract.State> {

    override suspend fun invoke(
        currentState: BrowserContract.State,
        action: BrowserContract.Action
    ): BrowserContract.State =
        when (action) {
            is BrowserContract.Action.ShowLoading ->
                currentState.copy(userState = UserState.Loading)
            is BrowserContract.Action.ShowUser ->
                currentState.copy(userState = UserState.Loaded(action.user))
            is BrowserContract.Action.ShowError ->
                currentState.copy(userState = UserState.Error(action.message))
        }
}