package com.luismunyoz.dirbrowser.app.browser.list

import com.luismunyoz.core.onError
import com.luismunyoz.core.onSuccess
import com.luismunyoz.domain.browser.GetFolderUseCase
import com.luismunyoz.mvi.framework.Actor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BrowserListActor @Inject constructor(
    private val getFolderUseCase: GetFolderUseCase
) : Actor<BrowserListContract.Event, BrowserListContract.Action> {

    override fun invoke(event: BrowserListContract.Event): Flow<BrowserListContract.Action> {
        return when (event) {
            is BrowserListContract.Event.OnInitialized -> loadFolder(event.id)
            BrowserListContract.Event.OnEffectConsumed -> flow { emit(BrowserListContract.Action.OnEffectConsumed) }
            is BrowserListContract.Event.OnItemClicked -> flow { emit(BrowserListContract.Action.NavigateToItem(event.id))}
        }
    }

    private fun loadFolder(id: String) = flow {
        emit(BrowserListContract.Action.ShowLoading)

        getFolderUseCase.invoke(id)
            .onSuccess {
                if(it.isEmpty()) {
                    emit(BrowserListContract.Action.ShowEmptyView)
                } else {
                    emit(BrowserListContract.Action.ShowFolder(it))
                }
            }
            .onError { emit(BrowserListContract.Action.ShowError(it.toString())) }
    }
}