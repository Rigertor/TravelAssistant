package ru.rigertor.smarttravelassistant.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.rigertor.smarttravelassistant.data.local.db.TripHistoryDao
import ru.rigertor.smarttravelassistant.data.mapper.toEntities
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val tripHistoryDao: TripHistoryDao
) : HistoryRepository {


    override val tripHistory: Flow<List<Trip>>
        get() = tripHistoryDao.getTripHistory().map { it.toEntities() }


    override suspend fun deleteHistoryTripItem(tripId: String) {
        tripHistoryDao.deleteFromHistory(tripId = tripId)
    }
}