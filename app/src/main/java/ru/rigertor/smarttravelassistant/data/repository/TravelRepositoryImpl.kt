package ru.rigertor.smarttravelassistant.data.repository

import ru.rigertor.smarttravelassistant.data.local.db.TripHistoryDao
import ru.rigertor.smarttravelassistant.data.mapper.escapeForPrompt
import ru.rigertor.smarttravelassistant.data.mapper.toDbBundle
import ru.rigertor.smarttravelassistant.data.mapper.toEntity
import ru.rigertor.smarttravelassistant.data.mapper.toTripDto
import ru.rigertor.smarttravelassistant.data.network.api.AiRequest
import ru.rigertor.smarttravelassistant.data.network.api.TripApiService
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.domain.repository.TravelRepository

class TravelRepositoryImpl(
    private val tripApiService: TripApiService,
    private val tripHistoryDao: TripHistoryDao
) : TravelRepository {

    override suspend fun buildTrip(
        promptTemplate: String,
        userQuery: String,
        specialCondition: String?,
        hotelBudget: String?,
        preferencesHotel: String?
    ): Trip {
        val safeQuery = userQuery.escapeForPrompt()
        val finalPrompt = promptTemplate
            .replace(USER_QUERY, safeQuery)
            .replace(SPECIAL_CONDITIONS, specialCondition ?: "")
            .replace(HOTEL_BUDGET, hotelBudget ?: "")
            .replace(PREFERENCES_HOTEL, preferencesHotel ?: "")
        val request = AiRequest(
            input = finalPrompt
        )
        val tripDto = tripApiService.createTrip(
            request = request
        ).toTripDto()
        val tripBundle = tripDto.toDbBundle()
        tripHistoryDao.saveTripBundle(tripBundle)
        return tripBundle.toEntity()
    }

    override suspend fun getImageUrlByQuery(imageQuery: String): String {
        TODO()
    }

    private companion object {
        const val USER_QUERY = "{{ТЕКСТ_ЗАПРОСА_ПОЛЬЗОВАТЕЛЯ_БЕЗ_ИЗМЕНЕНИЙ}}"
        const val SPECIAL_CONDITIONS = "{{ОСОБЫЕ_УСЛОВИЯ}}"
        const val HOTEL_BUDGET = "{{БЮДЖЕТ_НА_ОТЕЛЬ}}"
        const val PREFERENCES_HOTEL = "{{ПРЕДПОЧТЕНИЯ_ОТЕЛЬ}}"
    }
}