package ru.rigertor.smarttravelassistant.domain.repository

import ru.rigertor.smarttravelassistant.domain.entity.Trip

interface TravelRepository {

    suspend fun buildTrip(userRequest: String): Trip
}