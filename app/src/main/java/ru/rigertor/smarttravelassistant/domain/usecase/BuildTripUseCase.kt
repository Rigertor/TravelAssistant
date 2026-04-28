package ru.rigertor.smarttravelassistant.domain.usecase

import ru.rigertor.smarttravelassistant.domain.repository.TravelRepository

class BuildTripUseCase(
    private val repository: TravelRepository
) {


    suspend operator fun invoke(promptTemplate: String, userRequest: String) =
        repository.buildTrip(promptTemplate = promptTemplate, userQuery = userRequest)
}