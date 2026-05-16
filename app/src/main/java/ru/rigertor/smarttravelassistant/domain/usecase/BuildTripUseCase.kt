package ru.rigertor.smarttravelassistant.domain.usecase

import ru.rigertor.smarttravelassistant.domain.repository.TravelRepository
import javax.inject.Inject

class BuildTripUseCase @Inject constructor(
    private val repository: TravelRepository
) {


    suspend operator fun invoke(userRequest: String) =
        repository.buildTrip(userQuery = userRequest)
}