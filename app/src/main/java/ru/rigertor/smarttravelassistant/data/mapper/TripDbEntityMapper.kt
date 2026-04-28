package ru.rigertor.smarttravelassistant.data.mapper

import ru.rigertor.smarttravelassistant.data.local.model.DayDbModel
import ru.rigertor.smarttravelassistant.data.local.model.HotelDbModel
import ru.rigertor.smarttravelassistant.data.local.model.PlaceDbModel
import ru.rigertor.smarttravelassistant.data.local.model.TripBundle
import ru.rigertor.smarttravelassistant.data.local.relation.DayWithPlaces
import ru.rigertor.smarttravelassistant.data.local.relation.TripWithDetails
import ru.rigertor.smarttravelassistant.domain.entity.BaseHotel
import ru.rigertor.smarttravelassistant.domain.entity.DailyPlan
import ru.rigertor.smarttravelassistant.domain.entity.DailyRoute
import ru.rigertor.smarttravelassistant.domain.entity.Location
import ru.rigertor.smarttravelassistant.domain.entity.Place
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import java.util.Calendar
import java.util.Date


fun TripBundle.toEntity() = with(trip) {
    Trip(
        id = id,
        destination = destination,
        dates = dates,
        startTimestamp = startTimestamp,
        endTimestamp = endTimestamp,
        weatherForecast = weatherForecast,
        totalBudgetOnPerson = totalBudgetOnPerson,
        currency = currency,
        currencySymbol = currencySymbol,
        baseHotel = hotel.toBaseHotel(),
        days = days.map { it.toDailyPlan(places = places) },
        advice = advice
    )
}

fun TripWithDetails.toEntity() = with(trip) {
    Trip(
        id = id,
        destination = destination,
        dates = dates,
        startTimestamp = startTimestamp,
        endTimestamp = endTimestamp,
        weatherForecast = weatherForecast,
        totalBudgetOnPerson = totalBudgetOnPerson,
        currency = currency,
        currencySymbol = currencySymbol,
        baseHotel = hotel.toBaseHotel(),
        days = days.map { it.toDailyPlan() },
        advice = advice
    )
}

private fun DayDbModel.toDailyPlan(places: List<PlaceDbModel>) = DailyPlan(
    id = id,
    dayNumber = dayNumber,
    date = date.toCalendar(),
    theme = theme,
    weather = weather,
    places = places.map { it.toPlace() },
    dailyRout = DailyRoute(
        totalDistance = dailyRoutTotalDistance,
        transport = dailyRoutTransport,
        transportCost = dailyRoutTransportCost
    ),
    dailyTips = dailyTips
)

private fun DayWithPlaces.toDailyPlan() = with(day) {
    DailyPlan(
        id = id,
        dayNumber = dayNumber,
        date = date.toCalendar(),
        theme = theme,
        weather = weather,
        places = places.map { it.toPlace() },
        dailyRout = DailyRoute(
            totalDistance = dailyRoutTotalDistance,
            transport = dailyRoutTransport,
            transportCost = dailyRoutTransportCost
        ),
        dailyTips = dailyTips
    )
}

private fun HotelDbModel.toBaseHotel() = BaseHotel(
    id = id,
    name = name,
    nameLocal = nameLocal,
    location = Location(lat = lat, lng = lng),
    address = address,
    description = description,
    estimatedPricePerNight = estimatedPricePerNight,
    priceNote = priceNote,
    imageQuery = imageQuery
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun PlaceDbModel.toPlace() = Place(
    id = id,
    time = time,
    name = name,
    nameLocal = nameLocal,
    location = Location(lat = lat, lng = lng),
    address = address,
    description = description,
    durationHours = durationHours,
    estimatedCost = estimatedCost,
    costNote = costNote,
    placeType = placeType,
    imageQuery = imageQuery
)