package com.luismunyoz.dirbrowser.app.browser

import com.luismunyoz.domain.browser.model.User
import com.luismunyoz.mvi.framework.MviAction
import com.luismunyoz.mvi.framework.UiEvent
import com.luismunyoz.mvi.framework.UiState

class BrowserContract {

    sealed interface Event : UiEvent {
        object OnInitialized : Event
    }

    data class State(
        val userState: UserState
    ) : UiState {

        companion object {
            fun initial() = State(UserState.Loading)
        }
    }

    sealed interface Action : MviAction {

        object ShowLoading : Action
        data class ShowUser(val user: User) : Action
        data class ShowError(val message: String): Action
    }
}

sealed class UserState {
    object Loading : UserState()
    data class Loaded(val user: User) : UserState()
    data class Error(val error: String): UserState()
}