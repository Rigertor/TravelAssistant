package ru.rigertor.smarttravelassistant.data.local.model

data class TripBundle(
    val trip: TripDbModel,
    val hotel: HotelDbModel,
    val days: List<DayDbModel>,
    val places: List<PlaceDbModel>
)