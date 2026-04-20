package ru.rigertor.smarttravelassistant.domain.usecase

import ru.rigertor.smarttravelassistant.domain.repository.TravelRepository

class BuildTripUseCase(
    private val repository: TravelRepository
) {


    suspend operator fun invoke(userRequest: String) =
        repository.buildTrip(userRequest = userRequest)
}