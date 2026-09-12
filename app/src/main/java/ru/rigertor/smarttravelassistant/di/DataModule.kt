package ru.rigertor.smarttravelassistant.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.rigertor.smarttravelassistant.data.local.db.TripDatabase
import ru.rigertor.smarttravelassistant.data.local.db.TripHistoryDao
import ru.rigertor.smarttravelassistant.data.network.api.TripApiFactory
import ru.rigertor.smarttravelassistant.data.network.api.TripApiService
import ru.rigertor.smarttravelassistant.data.repository.HistoryRepositoryImpl
import ru.rigertor.smarttravelassistant.data.repository.ThemeRepositoryImpl
import ru.rigertor.smarttravelassistant.data.repository.TravelRepositoryImpl
import ru.rigertor.smarttravelassistant.domain.repository.HistoryRepository
import ru.rigertor.smarttravelassistant.domain.repository.ThemeRepository
import ru.rigertor.smarttravelassistant.domain.repository.TravelRepository

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindThemeRepository(impl: ThemeRepositoryImpl): ThemeRepository

    @[ApplicationScope Binds]
    fun bindTravelRepository(impl: TravelRepositoryImpl): TravelRepository

    @[ApplicationScope Binds]
    fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository


    companion object {

        @[ApplicationScope Provides]
        fun provideTripApiService(): TripApiService = TripApiFactory.apiService

        @[ApplicationScope Provides]
        fun provideHistoryDataBase(context: Context): TripDatabase =
            TripDatabase.getInstance(context = context)

        @[ApplicationScope Provides]
        fun provideTripHistoryDao(database: TripDatabase): TripHistoryDao =
            database.tripHistoryDao()
    }
}