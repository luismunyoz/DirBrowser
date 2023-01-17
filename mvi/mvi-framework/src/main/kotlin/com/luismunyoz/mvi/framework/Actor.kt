package com.luismunyoz.mvi.framework

import kotlinx.coroutines.flow.Flow

/**
 * Entity responsible for communication with any layer above presentation to retrieve the data needed to update UI
 * or make any changes in data structure of the app. Operates on dispatcher passed in view model constructor, since
 * it is intended to do heavy performance operations you should pass Dispatchers.IO as the value.
 * Performs some logic on every received [Event] and returns the [Flow] of [Action]s with the data needed to update state
 * later in [Reducer] or empty flow if there is nothing to update.
 *
 * Mandatory component for view model setup.
 */
fun interface Actor<Event : UiEvent, Action : MviAction> {

    /**
     * Method that is invoked on every received [Event].
     *
     * @param event received [Event].
     *
     * @return flow of [Action]s. In case no action is needed for particular event should return empty flow.
     */
    operator fun invoke(event: Event): Flow<Action>
}
