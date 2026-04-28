package ru.rigertor.smarttravelassistant.data.mapper

import com.google.gson.Gson
import ru.rigertor.smarttravelassistant.data.network.dto.TripDto
import ru.rigertor.smarttravelassistant.data.network.dto.TripResponseDto

fun TripResponseDto.toTripDto(): TripDto {
    val responseContent = outputContent.first()
    val tripId = responseContent.id

    val contentText = responseContent.content.first().contentText

    val trip = Gson().fromJson(contentText, TripDto::class.java)

    return trip.copy(id = tripId)
}

fun String.escapeForPrompt(): String =
    replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", " ")
        .replace("\r", " ")
