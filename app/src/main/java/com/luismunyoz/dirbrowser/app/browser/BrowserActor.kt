package com.luismunyoz.dirbrowser.app.browser

import com.luismunyoz.core.onError
import com.luismunyoz.core.onSuccess
import com.luismunyoz.domain.browser.GetUserUseCase
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.mvi.framework.Actor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BrowserActor @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : Actor<BrowserContract.Event, BrowserContract.Action> {

    override fun invoke(event: BrowserContract.Event): Flow<BrowserContract.Action> {
        return when (event) {
            BrowserContract.Event.OnInitialized -> loadUserFlow()
            BrowserContract.Event.OnEffectConsumed -> flow { emit(BrowserContract.Action.OnEffectConsumed) }
            is BrowserContract.Event.OnItemClicked -> itemClickedFlow(event.item)
        }
    }

    private fun loadUserFlow() = flow {
        emit(BrowserContract.Action.ShowLoading)

        getUserUseCase.invoke()
            .onSuccess { emit(BrowserContract.Action.ShowUser(it)) }
            .onError { emit(BrowserContract.Action.ShowError(it.toString())) }
    }

    private fun itemClickedFlow(item: Item) = flow {
        when(item) {
            is Item.Folder -> emit(BrowserContract.Action.NavigateToFolder(item))
            is Item.Image -> emit(BrowserContract.Action.NavigateToImage(item))
            else -> {}
        }
    }
}