package com.luismunyoz.mvi.framework

/**
 * Reducer is an entity responsible for updating current [State] based on the data received in [MviAction].
 * Works on main thread to ensure data consistency when it comes to access to the current [State].
 *
 * Mandatory component for view model setup.
 */
fun interface Reducer<Action : MviAction, State : UiState> {
    /**
     * Method that is invoked to update the [State] with each received [Action].
     *
     * @param currentState current [State] stored in view model.
     * @param action [Action] that comes from the [Actor].
     *
     * @return instance of [State] class.
     */
    suspend operator fun invoke(currentState: State, action: Action): State
}
