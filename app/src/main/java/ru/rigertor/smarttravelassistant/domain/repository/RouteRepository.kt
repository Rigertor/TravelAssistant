package ru.rigertor.smarttravelassistant.domain.repository

import ru.rigertor.smarttravelassistant.domain.entity.Location

interface RouteRepository {
    suspend fun getWalkingRoute(points: List<Location>): List<List<Location>>
}
