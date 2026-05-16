package ru.rigertor.smarttravelassistant.presentation.trip

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.rigertor.smarttravelassistant.domain.entity.DailyPlan
import ru.rigertor.smarttravelassistant.domain.entity.Place
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.presentation.trip.TripStore.Intent
import ru.rigertor.smarttravelassistant.presentation.trip.TripStore.Label
import ru.rigertor.smarttravelassistant.presentation.trip.TripStore.State
import javax.inject.Inject

interface TripStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent

        data class ClickDay(val day: DailyPlan) : Intent

        data class ClickPlace(val place: Place) : Intent

    }

    data class State(
        val trip: Trip,
        val currentDay: DailyPlan = trip.days.first()
    )

    sealed interface Label {
        data object ClickBack : Label

        data class ClickPlace(val place: Place) : Label

    }
}

class TripStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(trip: Trip): TripStore =
        object : TripStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TripStore",
            initialState = State(trip = trip),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeCurrentDay(val day: DailyPlan) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickDay -> {
                    dispatch(Msg.ChangeCurrentDay(day = intent.day))
                }

                is Intent.ClickPlace -> {
                    publish(Label.ClickPlace(place = intent.place))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeCurrentDay -> {
                    val currDay = msg.day
                    if (currentDay != currDay)
                        copy(currentDay = currDay)
                    else
                        this
                }
            }
    }
}
