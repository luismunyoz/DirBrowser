package com.luismunyoz.dirbrowser.app.browser.list

import com.luismunyoz.dirbrowser.app.browser.BrowserContract
import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.mvi.framework.MviAction
import com.luismunyoz.mvi.framework.UiEvent
import com.luismunyoz.mvi.framework.UiState

class BrowserListContract {

    sealed interface Event : UiEvent {
        object OnEffectConsumed: Event
        data class OnInitialized(val id: String) : Event
        data class OnItemClicked(val id: String): Event
    }

    data class State(
        val itemsState: ItemsState,
        val effect: Effect
    ) : UiState {

        companion object {
            fun initial() = State(ItemsState.Loading, Effect.None)
        }

        sealed class Effect {
            object None: Effect()
            data class NavigateToItem(val item: Item): Effect()
        }
    }

    sealed interface Action : MviAction {

        object ShowLoading : Action
        object ShowEmptyView : Action
        object OnEffectConsumed: Action
        data class ShowFolder(val items: List<Item>) : Action
        data class ShowError(val message: String) : Action
        data class NavigateToItem(val id: String): Action
    }
}

sealed class ItemsState {
    object Loading : ItemsState()
    object Empty : ItemsState()
    data class Loaded(val items: List<Item>) : ItemsState()
    data class Error(val error: String) : ItemsState()
}