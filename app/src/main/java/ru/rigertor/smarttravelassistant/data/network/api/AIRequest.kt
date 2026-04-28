package ru.rigertor.smarttravelassistant.data.network.api

import ru.rigertor.smarttravelassistant.BuildConfig

data class AiRequest(
//    @SerializedName("prompt")
    val prompt: Prompt = Prompt(),

//    @SerializedName("input")
    val input: String
)

data class Prompt(
//    @SerializedName("id")
    val id: String = BuildConfig.YANDEX_PROMPT_ID
)