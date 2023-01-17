package com.luismunyoz.mvi.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luismunyoz.mvi.framework.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Base class for improved MVI setup. It combines [Actor] and [Reducer] outputs in a single reactive
 * chain of transformations.
 * View model is ready to accept [Event]s as soon as there will be subscription for reactive chain in the
 * init block of the class. [Event]s are buffered in case some arrives but view model is not able to process them.
 */
@OptIn(FlowPreview::class)
abstract class MVIViewModel<State : UiState, Event : UiEvent, Action : MviAction>(
    val ioDispatcher: CoroutineDispatcher,
    initialState: State,
    actor: Actor<Event, Action>,
    reducer: Reducer<Action, State>,
    private val tag: String? = null,
) : ViewModel() {

    private val events = MutableSharedFlow<Event>()

    private val _state = MutableStateFlow(initialState)
    val state: Flow<State> = _state
        .asStateFlow()
        .onEach { newState -> Timber.tag(getTag()).v("UiState: $newState") }

    init {
        val tag = getTag()

        events.asSharedFlow()
            .onEach { Timber.tag(tag).v("UiEvent: $it") }
            .produceIn(viewModelScope)
            .consumeAsFlow()
            .onEach { Timber.tag(tag).v("Actor, UiEvent: $it") }
            .flatMapMerge { event -> actor(event) }
            .onEach { Timber.tag(tag).v("MviAction: $it") }
            .flowOn(ioDispatcher) // insures all logic in [Actor] runs on background dispatcher
            .scan(initialState) { currentState, action -> reducer(currentState, action) }
            .onEach { newState -> _state.update { newState } }
            .launchIn(viewModelScope)
    }

    fun getTag(): String = this.tag ?: this.javaClass.simpleName

    /** Emits a new intent, suspending on buffer overflow */
    fun onEvent(event: Event) {
        viewModelScope.launch { events.emit(event) }
    }
}
