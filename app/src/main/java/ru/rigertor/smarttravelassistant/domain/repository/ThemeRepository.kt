package ru.rigertor.smarttravelassistant.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.rigertor.smarttravelassistant.domain.entity.ThemeMode

interface ThemeRepository {

    val appTheme: Flow<ThemeMode>

    suspend fun setTheme(themeMode: ThemeMode)
}