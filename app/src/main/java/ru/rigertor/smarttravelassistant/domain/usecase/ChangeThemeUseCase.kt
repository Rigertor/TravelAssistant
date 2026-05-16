package ru.rigertor.smarttravelassistant.domain.usecase

import ru.rigertor.smarttravelassistant.domain.entity.ThemeMode
import ru.rigertor.smarttravelassistant.domain.repository.ThemeRepository
import javax.inject.Inject

class ChangeThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {

    suspend operator fun invoke(themeMode: ThemeMode) {
        themeRepository.setTheme(themeMode = themeMode)
    }
}