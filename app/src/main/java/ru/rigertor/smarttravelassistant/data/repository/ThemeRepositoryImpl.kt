package ru.rigertor.smarttravelassistant.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.rigertor.smarttravelassistant.data.local.datastore.dataStore
import ru.rigertor.smarttravelassistant.domain.entity.ThemeMode
import ru.rigertor.smarttravelassistant.domain.repository.ThemeRepository
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val context: Context
) : ThemeRepository {

    override val appTheme: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        ThemeMode.valueOf(
            prefs[THEME_KEY] ?: ThemeMode.SYSTEM.name
        )
    }

    override suspend fun setTheme(themeMode: ThemeMode) {
        context.dataStore.edit {
            it[THEME_KEY] = themeMode.name
        }
    }


    private companion object {
        val THEME_KEY = stringPreferencesKey("theme_mode")
    }
}