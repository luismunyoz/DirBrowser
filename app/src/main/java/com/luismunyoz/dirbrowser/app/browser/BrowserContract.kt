package com.luismunyoz.dirbrowser.app.browser

import com.luismunyoz.domain.browser.model.Item
import com.luismunyoz.domain.browser.model.User
import com.luismunyoz.mvi.framework.MviAction
import com.luismunyoz.mvi.framework.UiEvent
import com.luismunyoz.mvi.framework.UiState

class BrowserContract {

    sealed interface Event : UiEvent {
        object OnInitialized : Event
        object OnEffectConsumed: Event
        data class OnItemClicked(val item: Item): Event
    }

    data class State(
        val userState: UserState,
        val effect: Effect,
        val shownItem: Item?
    ) : UiState {

        companion object {
            fun initial() = State(UserState.Loading, Effect.None, null)
        }

        sealed class Effect {
            object None: Effect()
            data class NavigateToFolder(val folder: Item.Folder): Effect()
            data class NavigateToImage(val image: Item.Image): Effect()
        }
    }

    sealed interface Action : MviAction {

        object ShowLoading : Action
        object OnEffectConsumed: Action
        data class ShowUser(val user: User) : Action
        data class ShowError(val message: String): Action
        data class NavigateToFolder(val folder: Item.Folder): Action
        data class NavigateToImage(val image: Item.Image): Action
    }
}

sealed class UserState {
    object Loading : UserState()
    data class Loaded(val user: User) : UserState()
    data class Error(val error: String): UserState()
}