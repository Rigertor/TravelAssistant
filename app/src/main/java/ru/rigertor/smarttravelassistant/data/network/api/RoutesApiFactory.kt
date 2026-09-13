package ru.rigertor.smarttravelassistant.data.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RoutesApiFactory {
    fun create(): RoutesApiService = Retrofit.Builder()
        .baseUrl("https://api.openrouteservice.org/")
        .client(OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RoutesApiService::class.java)
}
