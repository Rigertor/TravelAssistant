package ru.rigertor.smarttravelassistant.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.rigertor.smarttravelassistant.domain.entity.Trip

interface HistoryRepository {

    val tripHistory: Flow<List<Trip>>

    suspend fun deleteHistoryTripItem(tripId: String)
}