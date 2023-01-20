package com.luismunyoz.mvi.android

import app.cash.turbine.testIn
import com.luismunyoz.mvi.framework.MviAction
import com.luismunyoz.mvi.framework.UiEvent
import com.luismunyoz.mvi.framework.UiState
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runCurrent
import org.junit.jupiter.api.Test

internal class MVIViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Test
    fun `should process incoming events`() {
        runTestWithDispatcher(testDispatcher) {
            val viewModel = createViewModel(
                state = State(),
                actor = { flowOf(Action()) },
                reducer = { _, _ -> State() }
            )

            val turbine = viewModel.state.testIn(this)

            viewModel.onEvent(Event.Event1)
            viewModel.onEvent(Event.Event2)

            runCurrent()

            turbine.cancelAndConsumeRemainingEvents().size shouldBe 3
        }
    }

    @Test
    fun `should handle each action in reducer`() {
        runTestWithDispatcher(testDispatcher) {
            val viewModel = createViewModel(
                state = State(),
                actor = { flowOf(Action(), Action(), Action()) },
                reducer = { _, _ -> State() }
            )

            val turbine = viewModel.state.testIn(this)

            viewModel.onEvent(Event.Event1)

            runCurrent()

            turbine.cancelAndConsumeRemainingEvents().size shouldBe 4
        }
    }

    private fun createViewModel(
        state: State,
        actor: (Event) -> Flow<Action>,
        reducer: (State, Action) -> State,
    ): MVIViewModel<State, Event, Action> {
        return object : MVIViewModel<State, Event, Action>(
            initialState = state,
            actor = actor,
            reducer = reducer,
            ioDispatcher = testDispatcher
        ) {

        }
    }

    sealed interface Event : UiEvent {
        object Event1 : Event
        object Event2 : Event
    }

    class Action : MviAction
    class State : UiState
}