package ru.rigertor.smarttravelassistant.data.mapper

import com.google.gson.Gson
import com.google.gson.JsonParseException
import ru.rigertor.smarttravelassistant.data.network.dto.TripDto
import ru.rigertor.smarttravelassistant.data.network.dto.TripResponseDto

fun TripResponseDto.toTripDto(): TripDto {
    if (status != null && status != "completed") {
        throw JsonParseException("Trip response is not completed")
    }
    val responseContent = outputContent.orEmpty().lastOrNull {
        it.type == "message" && it.role == "assistant"
    } ?: throw JsonParseException("Trip response has no assistant message")
    val tripId = responseContent.id

    val contentText = responseContent.content.orEmpty()
        .filter { it.type == "output_text" }
        .mapNotNull { it.contentText }
        .joinToString("")
    if (contentText.isBlank()) {
        throw JsonParseException("Trip response has no output text")
    }

    val trip = Gson().fromJson(contentText, TripDto::class.java)
        ?: throw JsonParseException("Trip response is null")

    return trip.copy(id = tripId, createdAt = System.currentTimeMillis())
}

fun String.escapeForPrompt(): String = trim()
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("\n", " ")
    .replace("\r", " ")
