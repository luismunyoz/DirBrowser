package com.luismunyoz.dirbrowser.app.browser.list

import com.luismunyoz.dirbrowser.app.di.IoDispatcher
import com.luismunyoz.mvi.android.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class BrowserListViewModel @Inject constructor(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    actor: BrowserListActor,
    reducer: BrowserListReducer
): MVIViewModel<BrowserListContract.State, BrowserListContract.Event, BrowserListContract.Action>(
    ioDispatcher = ioDispatcher,
    initialState = BrowserListContract.State.initial(),
    actor = actor,
    reducer = reducer
)