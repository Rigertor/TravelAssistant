package ru.rigertor.smarttravelassistant.domain.usecase

import ru.rigertor.smarttravelassistant.domain.entity.Location
import ru.rigertor.smarttravelassistant.domain.repository.RouteRepository
import javax.inject.Inject

class GetWalkingRouteUseCase @Inject constructor(
    private val repository: RouteRepository
) {
    suspend operator fun invoke(points: List<Location>) = repository.getWalkingRoute(points)
}
