package ru.rigertor.smarttravelassistant.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.rigertor.smarttravelassistant.domain.entity.ThemeMode
import ru.rigertor.smarttravelassistant.domain.repository.ThemeRepository
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {


    operator fun invoke(): Flow<ThemeMode> = themeRepository.appTheme
}