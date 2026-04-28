package ru.rigertor.smarttravelassistant.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Blue20,
    secondary = DarkGray40,
    tertiary = Purple30,
    background = DarkBlue10,
    surface = DarkGray20,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = LightGray80,
    onSurface = LightGray80,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue20,
    secondary = LightBlue80,
    tertiary = LightGreen20,
    background = LightBlue90,
    surface = White,
    onPrimary = White,
    onSecondary = DarkBlue10,
    onTertiary = DarkBlue10,
    onBackground = DarkBlue10,
    onSurface = DarkBlue10,
)

val LocalBackgroundGradient = staticCompositionLocalOf<Brush> {
    error("No background gradient provided")
}

@Composable
fun SmartTravelAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val backgroundGradient = Brush.linearGradient(
        colors = if (darkTheme) listOf(DarkBlue80, DarkBlue90)
        else listOf(Blue20, Blue30)
    )
    CompositionLocalProvider(
        LocalBackgroundGradient provides backgroundGradient
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}