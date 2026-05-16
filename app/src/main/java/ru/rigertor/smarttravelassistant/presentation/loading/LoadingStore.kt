package ru.rigertor.smarttravelassistant.presentation.loading

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.domain.usecase.BuildTripUseCase
import ru.rigertor.smarttravelassistant.presentation.loading.LoadingStore.Intent
import ru.rigertor.smarttravelassistant.presentation.loading.LoadingStore.Label
import ru.rigertor.smarttravelassistant.presentation.loading.LoadingStore.State
import javax.inject.Inject

interface LoadingStore : Store<Intent, State, Label> {

    sealed interface Intent

    data class State(
        val prompt: String,
        val loadingState: LoadingState
    ) {

        sealed interface LoadingState {

            data object Initial : LoadingState

            data object Loading : LoadingState

            data object Error : LoadingState

            data class Loaded(val trip: Trip) : LoadingState
        }
    }

    sealed interface Label {

        data object Loading : Label

        data object Error : Label

        data class Loaded(val trip: Trip) : Label
    }
}

class LoadingStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val buildTripUseCase: BuildTripUseCase
) {

    fun create(prompt: String): LoadingStore =
        object : LoadingStore, Store<Intent, State, Label> by storeFactory.create(
            name = "LoadingStore",
            initialState = State(prompt = prompt, loadingState = State.LoadingState.Initial),
            bootstrapper = BootstrapperImpl(prompt = prompt),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class TripLoaded(val trip: Trip) : Action

        data object TripStartLoading : Action

        data object TripLoadingError : Action
    }

    private sealed interface Msg {

        data object TripStartLoading : Msg

        data object TripLoadingError : Msg
    }

    private inner class BootstrapperImpl(private val prompt: String) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.TripStartLoading)
                try {
                    val trip = buildTripUseCase(
                        userRequest = prompt
                    )
                    Log.d("LoadingStore", trip.toString())
                    dispatch(Action.TripLoaded(trip = trip))
                } catch (_: Exception) {
                    dispatch(Action.TripLoadingError)
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.TripLoaded -> {
                    publish(Label.Loaded(trip = action.trip))
                }

                Action.TripLoadingError -> {
                    dispatch(Msg.TripLoadingError)
                }

                Action.TripStartLoading -> {
                    dispatch(Msg.TripStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {

            Msg.TripLoadingError -> {
                copy(loadingState = State.LoadingState.Error)
            }

            Msg.TripStartLoading -> {
                copy(loadingState = State.LoadingState.Loading)
            }
        }
    }
}