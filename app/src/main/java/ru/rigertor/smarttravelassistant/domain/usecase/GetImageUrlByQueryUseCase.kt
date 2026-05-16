package ru.rigertor.smarttravelassistant.domain.usecase

import ru.rigertor.smarttravelassistant.domain.repository.TravelRepository
import javax.inject.Inject

class GetImageUrlByQueryUseCase @Inject constructor(
    private val repository: TravelRepository
) {

    suspend operator fun invoke(imageQuery: String): String =
        repository.getImageUrlByQuery(imageQuery = imageQuery)
}