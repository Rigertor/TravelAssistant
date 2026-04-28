package ru.rigertor.smarttravelassistant.data.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.rigertor.smarttravelassistant.BuildConfig
import java.util.concurrent.TimeUnit

object TripApiFactory {

    private val TIME_TYPE = TimeUnit.SECONDS
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 60L
    private const val WRITE_TIMEOUT = 30L

    private const val ALL_TIMEOUT = CONNECT_TIMEOUT + READ_TIMEOUT + WRITE_TIMEOUT

    private const val KEY_PARAM = "Authorization"
    private const val FOLDER_PARAM = "OpenAI-Project"
    private const val CONTENT_TYPE_PARAM = "Content-Type"
    private const val CONTENT_TYPE = "application/json"

    private const val BASE_URL = "https://ai.api.cloud.yandex.net/v1/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader(CONTENT_TYPE_PARAM, CONTENT_TYPE)
                .addHeader(KEY_PARAM, "Api-Key ${BuildConfig.AI_API_KEY}")
                .addHeader(FOLDER_PARAM, BuildConfig.YANDEX_FOLDER_ID)
                .build()
            chain.proceed(request = newRequest)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(CONNECT_TIMEOUT, TIME_TYPE)  // время на установку соединения
        .readTimeout(READ_TIMEOUT, TIME_TYPE)     // время на чтение данных (включая заголовки)
        .writeTimeout(WRITE_TIMEOUT, TIME_TYPE)    // время на отправку тела запроса
        .callTimeout(ALL_TIMEOUT, TIME_TYPE)    // общий лимит на весь вызов (с OkHttp 4.x)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: TripApiService = retrofit.create()
}