package ru.rigertor.smarttravelassistant.presentation.trip

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.rigertor.smarttravelassistant.domain.entity.DailyPlan
import ru.rigertor.smarttravelassistant.domain.entity.Place
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.presentation.extensions.componentScope

class DefaultTripComponent @AssistedInject constructor(
    private val tripStoreFactory: TripStoreFactory,
    @Assisted("trip") private val trip: Trip,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onBackClicked") private val onPlaceClicked: (Place) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : TripComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { tripStoreFactory.create(trip = trip) }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    TripStore.Label.ClickBack -> onBackClicked()

                    is TripStore.Label.ClickPlace -> onPlaceClicked(it.place)
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<TripStore.State> = store.stateFlow

    override fun onRetryRoute() {
        store.accept(TripStore.Intent.RetryRoute)
    }

    override fun onClickBack() {
        store.accept(TripStore.Intent.ClickBack)
    }

    override fun onClickDay(day: DailyPlan) {
        store.accept(TripStore.Intent.ClickDay(day = day))
    }

    override fun onClickPlace(place: Place) {
        store.accept(TripStore.Intent.ClickPlace(place = place))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("trip") trip: Trip,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onBackClicked") onPlaceClicked: (Place) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultTripComponent
    }
}