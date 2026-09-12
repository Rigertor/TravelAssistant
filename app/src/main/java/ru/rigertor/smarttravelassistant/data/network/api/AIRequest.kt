package ru.rigertor.smarttravelassistant.data.network.api

import ru.rigertor.smarttravelassistant.BuildConfig

data class AiRequest(
    val prompt: Prompt = Prompt(),

    val input: String
)

data class Prompt(
    val id: String = BuildConfig.YANDEX_PROMPT_ID
)