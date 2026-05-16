package ru.rigertor.smarttravelassistant.domain.usecase.history

import kotlinx.coroutines.flow.Flow
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.domain.repository.HistoryRepository
import javax.inject.Inject

class GetTripHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    operator fun invoke(): Flow<List<Trip>> = repository.tripHistory

}