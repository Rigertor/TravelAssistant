package ru.rigertor.smarttravelassistant.presentation.start

import kotlinx.coroutines.flow.StateFlow

interface StartComponent {

    val model: StateFlow<StartStore.State>

    fun onHistoryClick()

    fun onStartClick()
}