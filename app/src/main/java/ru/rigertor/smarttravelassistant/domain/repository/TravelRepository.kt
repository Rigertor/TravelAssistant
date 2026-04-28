package ru.rigertor.smarttravelassistant.domain.repository

import ru.rigertor.smarttravelassistant.domain.entity.Trip

interface TravelRepository {

    suspend fun buildTrip(
        promptTemplate: String,
        userQuery: String,
        specialCondition: String? = null,
        hotelBudget: String? = null,
        preferencesHotel: String? = null
    ): Trip

    suspend fun getImageUrlByQuery(imageQuery: String): String
}