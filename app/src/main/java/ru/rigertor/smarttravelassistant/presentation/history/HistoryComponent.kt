package ru.rigertor.smarttravelassistant.presentation.history

import kotlinx.coroutines.flow.StateFlow
import ru.rigertor.smarttravelassistant.domain.entity.Trip

interface HistoryComponent {


    val model: StateFlow<HistoryStore.State>

    fun onBackClick()

    fun onHistoryTripClick(trip: Trip)

    fun onHistoryTripDelete(tripId: String)
}