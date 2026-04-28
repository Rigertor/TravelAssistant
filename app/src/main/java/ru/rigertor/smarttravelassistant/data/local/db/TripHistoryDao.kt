package ru.rigertor.smarttravelassistant.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.rigertor.smarttravelassistant.data.local.model.DayDbModel
import ru.rigertor.smarttravelassistant.data.local.model.HotelDbModel
import ru.rigertor.smarttravelassistant.data.local.model.PlaceDbModel
import ru.rigertor.smarttravelassistant.data.local.model.TripDbModel
import ru.rigertor.smarttravelassistant.data.local.model.TripBundle
import ru.rigertor.smarttravelassistant.data.local.relation.TripWithDetails

@Dao
interface TripHistoryDao {

    @Transaction
    @Query("SELECT * FROM trip_history ORDER BY createdAt DESC LIMIT 1")
    fun getLastTrip(): Flow<TripWithDetails?>

    @Transaction
    @Query("SELECT * FROM trip_history WHERE id = :tripId LIMIT 1")
    fun getTripById(tripId: String): Flow<TripWithDetails?>

    @Transaction
    @Query("SELECT * FROM trip_history ORDER BY createdAt")
    fun getTripHistory(): Flow<List<TripWithDetails>>

    @Query("DELETE FROM trip_history WHERE id=:tripId")
    suspend fun deleteFromHistory(tripId: String)

    @Transaction
    suspend fun saveTripBundle(tripBundle: TripBundle) {
        insertTrip(tripBundle.trip)
        insertHotel(tripBundle.hotel)
        insertDays(tripBundle.days)
        insertPlaces(tripBundle.places)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotel(hotel: HotelDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(days: List<DayDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceDbModel>)
}