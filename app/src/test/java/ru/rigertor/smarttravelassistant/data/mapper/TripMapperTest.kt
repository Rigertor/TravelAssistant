package ru.rigertor.smarttravelassistant.data.mapper

import com.google.gson.Gson
import com.google.gson.JsonParseException
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rigertor.smarttravelassistant.data.network.dto.TripResponseDto

class TripMapperTest {

    private val gson = Gson()
    private val tripJson = """
        {
          "destination": "Test city",
          "dates": "13–15 September 2026",
          "date_start_timestamp": 1789257600,
          "date_end_timestamp": 1789430400,
          "weather_forecast": "Sunny",
          "total_estimated_budget_per_person": 1000,
          "currency": "RUB",
          "currency_symbol": "₽",
          "base_hotel": {},
          "days": [],
          "general_advice": "Test advice"
        }
    """.trimIndent()

    @Test
    fun parsesMessageText() {
        val trip = response(message()).toTripDto()

        assertEquals("message-id", trip.id)
        assertEquals("Test city", trip.destination)
        assertEquals(1789257600L, trip.startTimestamp)
    }

    @Test
    fun skipsToolCallsBeforeMessage() {
        val trip = response("""
            {"id":"search-id","type":"web_search_call","status":"completed"},
            ${message()}
        """).toTripDto()

        assertEquals("message-id", trip.id)
        assertEquals("Test city", trip.destination)
    }

    @Test(expected = JsonParseException::class)
    fun rejectsIncompleteResponseEvenWithValidJson() {
        val response = response(message()).copy(status = "incomplete")

        response.toTripDto()
    }

    @Test(expected = JsonParseException::class)
    fun rejectsEmptyOutput() {
        response("").toTripDto()
    }

    @Test(expected = JsonParseException::class)
    fun rejectsRefusalWithoutText() {
        response("""
            {
              "id":"message-id", "type":"message", "role":"assistant",
              "content":[{"type":"refusal","refusal":"Cannot create a trip"}]
            }
        """).toTripDto()
    }

    @Test
    fun usesFinalMessageAfterCommentary() {
        val trip = response("""
            {
              "id":"commentary-id", "type":"message", "role":"assistant",
              "content":[{"type":"output_text","text":"Planning the trip"}]
            },
            ${message()}
        """).toTripDto()

        assertEquals("message-id", trip.id)
    }

    private fun message() = """
        {
          "id": "message-id",
          "type": "message",
          "role": "assistant",
          "status": "completed",
          "content": [{"type":"output_text","text":${gson.toJson(tripJson)}}]
        }
    """.trimIndent()

    private fun response(output: String): TripResponseDto = gson.fromJson(
        """{"status":"completed","output":[$output]}""",
        TripResponseDto::class.java
    )
}
