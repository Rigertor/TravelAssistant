package ru.rigertor.smarttravelassistant.data.mapper

import ru.rigertor.smarttravelassistant.data.local.model.DayDbModel
import ru.rigertor.smarttravelassistant.data.local.model.HotelDbModel
import ru.rigertor.smarttravelassistant.data.local.model.PlaceDbModel
import ru.rigertor.smarttravelassistant.data.local.model.TripBundle
import ru.rigertor.smarttravelassistant.data.local.model.TripDbModel
import ru.rigertor.smarttravelassistant.data.network.dto.TripDto
import java.util.UUID

fun TripDto.toDbBundle(): TripBundle {

    val tripId = id

    val trip = TripDbModel(
        id = tripId,
        destination = destination,
        dates = dates,
        startTimestamp = startTimestamp,
        endTimestamp = endTimestamp,
        weatherForecast = weatherForecast,
        totalBudgetOnPerson = totalBudgetOnPerson,
        currency = currency,
        currencySymbol = currencySymbol,
        advice = advice
    )

    val hotel = HotelDbModel(
        id = UUID.randomUUID().toString(),
        tripId = tripId,
        name = baseHotel.name,
        nameLocal = baseHotel.nameLocal,
        address = baseHotel.address,
        lat = baseHotel.lat,
        lng = baseHotel.lng,
        description = baseHotel.description,
        imageQuery = baseHotel.imageQuery,
        estimatedPricePerNight = baseHotel.estimatedPricePerNight,
        priceNote = baseHotel.priceNote
    )

    val days = mutableListOf<DayDbModel>()
    val places = mutableListOf<PlaceDbModel>()

    this.days.forEach { dayDto ->

        val dayId = UUID.randomUUID().toString()

        days.add(
            DayDbModel(
                id = dayId,
                tripId = tripId,
                dayNumber = dayDto.dayNumber,
                date = dayDto.date,
                theme = dayDto.theme,
                weather = dayDto.weather,
                dailyTips = dayDto.dailyTips,
                dailyRoutTransport = dayDto.dailyRout.transport,
                dailyRoutTransportCost = dayDto.dailyRout.transportCost,
                dailyRoutTotalDistance = dayDto.dailyRout.totalDistance
            )
        )

        dayDto.places.forEach { placeDto ->
            places.add(
                PlaceDbModel(
                    id = UUID.randomUUID().toString(),
                    dayId = dayId,
                    name = placeDto.name,
                    nameLocal = placeDto.nameLocal,
                    lat = placeDto.lat,
                    lng = placeDto.lng,
                    time = placeDto.time,
                    durationHours = placeDto.durationHours,
                    placeType = placeDto.placeType,
                    estimatedCost = placeDto.estimatedCost,
                    costNote = placeDto.costNote,
                    description = placeDto.description,
                    address = placeDto.address,
                    imageQuery = placeDto.imageQuery
                )
            )
        }
    }

    return TripBundle(trip, hotel, days, places)
}