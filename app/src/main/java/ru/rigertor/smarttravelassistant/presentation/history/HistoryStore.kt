package ru.rigertor.smarttravelassistant.presentation.history

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.domain.usecase.history.DeleteHistoryTripUseCase
import ru.rigertor.smarttravelassistant.domain.usecase.history.GetTripHistoryUseCase
import ru.rigertor.smarttravelassistant.presentation.history.HistoryStore.Intent
import ru.rigertor.smarttravelassistant.presentation.history.HistoryStore.Label
import ru.rigertor.smarttravelassistant.presentation.history.HistoryStore.State
import ru.rigertor.smarttravelassistant.presentation.history.HistoryStore.State.HistoryState.Error
import ru.rigertor.smarttravelassistant.presentation.history.HistoryStore.State.HistoryState.Loading
import ru.rigertor.smarttravelassistant.presentation.history.HistoryStore.State.HistoryState.SuccessLoaded
import javax.inject.Inject

interface HistoryStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent

        data class DeleteTripClick(val tripId: String) : Intent

        data class HistoryTripClick(val trip: Trip) : Intent
    }

    data class State(
        val historyTripList: List<Trip>,
        val historyState: HistoryState
    ) {

        sealed interface HistoryState {

            data object Initial : HistoryState

            data object Loading : HistoryState

            data object Error : HistoryState

            data object EmptyResult : HistoryState

            data class SuccessLoaded(val trips: List<Trip>) : HistoryState

        }
    }

    sealed interface Label {

        data object ClickBack : Label

        data class OpenTrip(val trip: Trip) : Label
    }
}

class HistoryStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getTripHistoryUseCase: GetTripHistoryUseCase,
    private val deleteHistoryTripUseCase: DeleteHistoryTripUseCase
) {

    fun create(): HistoryStore =
        object : HistoryStore, Store<Intent, State, Label> by storeFactory.create(
            name = "HistoryStore",
            initialState = State(
                emptyList(),
                historyState = State.HistoryState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class HistoryLoaded(val tripHistory: List<Trip>) : Action
    }

    private sealed interface Msg {

        data object HistoryLoading : Msg

        data object HistoryLoadingError : Msg

        data class HistoryLoaded(val tripHistory: List<Trip>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getTripHistoryUseCase().collect {
                    dispatch(Action.HistoryLoaded(tripHistory = it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.DeleteTripClick -> {
                    scope.launch {
                        deleteHistoryTripUseCase(tripId = intent.tripId)
                    }
                }

                is Intent.HistoryTripClick -> {
                    publish(Label.OpenTrip(trip = intent.trip))
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.HistoryLoaded -> {
                    dispatch(Msg.HistoryLoaded(tripHistory = action.tripHistory))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.HistoryLoaded -> {
                val state = msg.tripHistory
                if (state.isNotEmpty())
                    copy(
                        historyTripList = msg.tripHistory,
                        historyState = SuccessLoaded(trips = msg.tripHistory)
                    )
                else
                    copy(historyState = State.HistoryState.EmptyResult)
            }

            Msg.HistoryLoading -> {
                copy(
                    historyState = Loading
                )
            }

            Msg.HistoryLoadingError -> {
                copy(historyState = Error)
            }
        }
    }
}