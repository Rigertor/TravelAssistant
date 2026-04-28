package ru.rigertor.smarttravelassistant.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.rigertor.smarttravelassistant.data.local.model.DayDbModel
import ru.rigertor.smarttravelassistant.data.local.model.HotelDbModel
import ru.rigertor.smarttravelassistant.data.local.model.PlaceDbModel
import ru.rigertor.smarttravelassistant.data.local.model.TripDbModel

@Database(
    entities = [
        TripDbModel::class,
        HotelDbModel::class,
        DayDbModel::class,
        PlaceDbModel::class
    ], version = 1, exportSchema = false
)
abstract class TripDatabase : RoomDatabase() {

    abstract fun tripHistoryDao(): TripHistoryDao

    companion object {

        private const val DB_NAME = "TripHistoryDatabase"

        private var INSTANCE: TripDatabase? = null

        private val LOCK = Any()

        fun getInstance(context: Context): TripDatabase {
            INSTANCE?.let { return it }
            synchronized(lock = LOCK) {
                INSTANCE?.let { return it }
                val database = Room.databaseBuilder(
                    context = context,
                    klass = TripDatabase::class.java,
                    name = DB_NAME
                ).build()
                INSTANCE = database
                return database
            }
        }
    }
}