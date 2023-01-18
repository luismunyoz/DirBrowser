package com.luismunyoz.dirbrowser.app.browser

import com.luismunyoz.dirbrowser.app.di.IoDispatcher
import com.luismunyoz.mvi.android.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    actor: BrowserActor,
    reducer: BrowserReducer
): MVIViewModel<BrowserContract.State, BrowserContract.Event, BrowserContract.Action>(
    ioDispatcher = ioDispatcher,
    initialState = BrowserContract.State.initial(),
    actor = actor,
    reducer = reducer
) {

    init {
        onEvent(event = BrowserContract.Event.OnInitialized)
    }
}