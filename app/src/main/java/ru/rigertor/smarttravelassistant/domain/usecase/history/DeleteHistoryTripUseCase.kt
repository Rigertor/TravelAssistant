package ru.rigertor.smarttravelassistant.domain.usecase.history

import ru.rigertor.smarttravelassistant.domain.repository.HistoryRepository
import javax.inject.Inject

class DeleteHistoryTripUseCase @Inject constructor(
    private val repository: HistoryRepository
) {


    suspend operator fun invoke(tripId: String) = repository.deleteHistoryTripItem(tripId = tripId)
}