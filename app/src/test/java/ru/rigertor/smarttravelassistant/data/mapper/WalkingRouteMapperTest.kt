package ru.rigertor.smarttravelassistant.data.mapper

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rigertor.smarttravelassistant.data.network.dto.WalkingRouteResponseDto
import ru.rigertor.smarttravelassistant.domain.entity.Location

class WalkingRouteMapperTest {
    @Test
    fun sendsLongitudeBeforeLatitudeInVisitOrder() {
        val request = listOf(Location(55.75, 37.61), Location(55.76, 37.62))
            .toWalkingRouteRequest()

        assertEquals(listOf(listOf(37.61, 55.75), listOf(37.62, 55.76)), request.coordinates)
        assertEquals(false, request.instructions)
    }

    @Test
    fun readsGeoJsonCoordinatesAndIgnoresOptionalElevation() {
        val route = response("""
            {"features":[{"geometry":{"type":"LineString",
            "coordinates":[[37.61,55.75,150],[37.62,55.76,151]]}}]}
        """).toRoutePoints()

        assertEquals(listOf(Location(55.75, 37.61), Location(55.76, 37.62)), route)
    }

    @Test(expected = IllegalStateException::class)
    fun rejectsMissingRoutes() {
        response("""{"features":[]}""").toRoutePoints()
    }

    @Test(expected = IllegalStateException::class)
    fun rejectsWrongGeometryType() {
        response("""{"features":[{"geometry":{"type":"Point","coordinates":[]}}]}""")
            .toRoutePoints()
    }

    @Test(expected = IllegalStateException::class)
    fun rejectsIncompleteCoordinates() {
        response("""{"features":[{"geometry":{"type":"LineString","coordinates":[[37],[38,55]]}}]}""")
            .toRoutePoints()
    }

    private fun response(json: String) = Gson().fromJson(json, WalkingRouteResponseDto::class.java)
}
