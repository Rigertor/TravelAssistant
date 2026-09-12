package ru.rigertor.smarttravelassistant.presentation.history

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
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.presentation.extensions.componentScope

class DefaultHistoryComponent @AssistedInject constructor(
    private val historyStoreFactory: HistoryStoreFactory,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onHistoryItemClicked") private val onHistoryItemClicked: (Trip) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : HistoryComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { historyStoreFactory.create() }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    HistoryStore.Label.ClickBack -> {
                        onBackClicked()
                    }

                    is HistoryStore.Label.OpenTrip -> {
                        onHistoryItemClicked(it.trip)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<HistoryStore.State> = store.stateFlow

    override fun onBackClick() {
        store.accept(intent = HistoryStore.Intent.ClickBack)
    }

    override fun onHistoryTripClick(trip: Trip) {
        store.accept(intent = HistoryStore.Intent.HistoryTripClick(trip = trip))
    }

    override fun onHistoryTripDelete(tripId: String) {
        store.accept(intent = HistoryStore.Intent.DeleteTripClick(tripId = tripId))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onHistoryItemClicked") onHistoryItemClicked: (Trip) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultHistoryComponent
    }
}