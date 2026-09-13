package ru.rigertor.smarttravelassistant.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rigertor.smarttravelassistant.data.local.model.DayDbModel
import ru.rigertor.smarttravelassistant.data.local.model.HotelDbModel
import ru.rigertor.smarttravelassistant.data.local.model.PlaceDbModel
import ru.rigertor.smarttravelassistant.data.local.model.TripBundle
import ru.rigertor.smarttravelassistant.data.local.model.TripDbModel
import ru.rigertor.smarttravelassistant.data.local.relation.DayWithPlaces
import ru.rigertor.smarttravelassistant.data.local.relation.TripWithDetails

class TripDbEntityMapperTest {
    private val bundle = TripBundle(
        trip = TripDbModel("trip", "City", "Dates", 0, 0, "Sunny", 0, "RUB", "₽", ""),
        hotel = HotelDbModel("hotel", "trip", "Hotel", "Hotel", 0.0, 0.0, "", "", 0, "", ""),
        days = listOf(day("first", 1), day("second", 2), day("empty", 3)),
        places = listOf(place("a", "first"), place("b", "second"), place("c", "first"))
    )

    @Test
    fun assignsOnlyMatchingPlacesToEachDayInVisitOrder() {
        val trip = bundle.toEntity()

        assertEquals(listOf("a", "c"), trip.days[0].places.map { it.id })
        assertEquals(listOf("b"), trip.days[1].places.map { it.id })
        assertEquals(emptyList<String>(), trip.days[2].places.map { it.id })
    }

    @Test
    fun generatedTripHasSameDaysAsHistoryTrip() {
        val history = TripWithDetails(
            trip = bundle.trip,
            hotel = bundle.hotel,
            days = bundle.days.map { day ->
                DayWithPlaces(day, bundle.places.filter { it.dayId == day.id })
            }
        )

        assertEquals(history.toEntity().days, bundle.toEntity().days)
    }

    private fun day(id: String, number: Int) = DayDbModel(
        id, "trip", number, 0, "", "", "", 0f, "walk", 0
    )

    private fun place(id: String, dayId: String) = PlaceDbModel(
        id, dayId, id, "10:00", id, 0.0, 0.0, "", "", 1f, 0, "", "", ""
    )
}
