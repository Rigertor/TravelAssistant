package ru.rigertor.smarttravelassistant.presentation.loading

import kotlinx.coroutines.flow.StateFlow

interface LoadingComponent {

    val model: StateFlow<LoadingStore.State>
}