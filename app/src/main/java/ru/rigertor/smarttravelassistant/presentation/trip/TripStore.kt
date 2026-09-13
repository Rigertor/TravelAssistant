package ru.rigertor.smarttravelassistant.presentation.trip

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import ru.rigertor.smarttravelassistant.domain.entity.DailyPlan
import ru.rigertor.smarttravelassistant.domain.entity.Location
import ru.rigertor.smarttravelassistant.domain.entity.Place
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.domain.usecase.GetWalkingRouteUseCase
import ru.rigertor.smarttravelassistant.presentation.trip.TripStore.Intent
import ru.rigertor.smarttravelassistant.presentation.trip.TripStore.Label
import ru.rigertor.smarttravelassistant.presentation.trip.TripStore.State

interface TripStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object RetryRoute : Intent

        data object ClickBack : Intent

        data class ClickDay(val day: DailyPlan) : Intent

        data class ClickPlace(val place: Place) : Intent

    }

    data class State(
        val trip: Trip,
        val currentDay: DailyPlan = trip.days.first(),
        val route: List<List<Location>> = emptyList(),
        val routeLoading: Boolean = false,
        val routeError: Boolean = false
    )

    sealed interface Label {
        data object ClickBack : Label

        data class ClickPlace(val place: Place) : Label

    }
}

class TripStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWalkingRouteUseCase: GetWalkingRouteUseCase
) {

    fun create(trip: Trip): TripStore =
        object : TripStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TripStore",
            initialState = State(trip = trip),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object LoadRoute : Action
    }

    private sealed interface Msg {
        data object RouteLoading : Msg
        data class RouteLoaded(val route: List<List<Location>>) : Msg
        data object RouteFailed : Msg

        data class ChangeCurrentDay(val day: DailyPlan) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.LoadRoute)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        private var routeJob: Job? = null
        private val routeCache = mutableMapOf<String, List<List<Location>>>()

        override fun executeAction(action: Action) {
            when (action) {
                Action.LoadRoute -> loadRoute()
            }
        }

        private fun loadRoute() {
            routeJob?.cancel()
            val day = state().currentDay
            dispatch(Msg.RouteLoading)
            val cached = routeCache[day.id]
            if (cached != null) {
                dispatch(Msg.RouteLoaded(cached))
                return
            }
            routeJob = scope.launch {
                try {
                    val route = getWalkingRouteUseCase(day.places.map { it.location })
                    ensureActive()
                    routeCache[day.id] = route
                    dispatch(Msg.RouteLoaded(route))
                } catch (exception: CancellationException) {
                    throw exception
                } catch (_: Exception) {
                    ensureActive()
                    dispatch(Msg.RouteFailed)
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.RetryRoute -> loadRoute()
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickDay -> {
                    if (state().currentDay != intent.day) {
                        dispatch(Msg.ChangeCurrentDay(day = intent.day))
                        loadRoute()
                    }
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
                Msg.RouteLoading -> copy(route = emptyList(), routeLoading = true, routeError = false)
                is Msg.RouteLoaded -> copy(route = msg.route, routeLoading = false, routeError = false)
                Msg.RouteFailed -> copy(route = emptyList(), routeLoading = false, routeError = true)
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
