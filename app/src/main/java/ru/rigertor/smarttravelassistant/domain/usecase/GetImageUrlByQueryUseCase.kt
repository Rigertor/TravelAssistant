package ru.rigertor.smarttravelassistant.domain.usecase

import ru.rigertor.smarttravelassistant.domain.repository.TravelRepository

class GetImageUrlByQueryUseCase(
    private val repository: TravelRepository
) {

    suspend operator fun invoke(imageQuery: String): String =
        repository.getImageUrlByQuery(imageQuery = imageQuery)
}