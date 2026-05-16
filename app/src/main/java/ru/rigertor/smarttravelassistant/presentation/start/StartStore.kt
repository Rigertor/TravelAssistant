package ru.rigertor.smarttravelassistant.presentation.start

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.rigertor.smarttravelassistant.presentation.start.StartStore.Intent
import ru.rigertor.smarttravelassistant.presentation.start.StartStore.Label
import ru.rigertor.smarttravelassistant.presentation.start.StartStore.State
import javax.inject.Inject

interface StartStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickHistory : Intent

        data object ClickStart : Intent
    }

    data class State(val todo: Unit)

    sealed interface Label {

        data object ClickHistory : Label

        data object ClickStart : Label
    }
}

class StartStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): StartStore =
        object : StartStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartStore",
            initialState = State(Unit),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {}
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ClickHistory -> {
                    publish(Label.ClickHistory)
                }

                Intent.ClickStart -> {
                    publish(Label.ClickStart)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = State(Unit)
    }
}
