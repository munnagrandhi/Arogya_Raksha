package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MedicalNavyDark,
    onPrimary = MedicalNavyOnDark,
    primaryContainer = MedicalNavyDarkContainer,
    onPrimaryContainer = MedicalNavyDarkOnContainer,
    secondary = MedicalTealDark,
    secondaryContainer = MedicalTealDarkContainer,
    tertiary = EmergencyAmber,
    error = EmergencyRed,
    errorContainer = EmergencyRedDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    outline = OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = MedicalNavyLight,
    onPrimary = MedicalNavyOnLight,
    primaryContainer = MedicalNavyContainer,
    onPrimaryContainer = MedicalNavyOnContainer,
    secondary = MedicalTealLight,
    secondaryContainer = MedicalTealContainer,
    tertiary = EmergencyAmber,
    error = EmergencyRed,
    errorContainer = EmergencyRedContainer,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    outline = OutlineLight
)

@Composable
fun AarogyaRakshaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our curated medical colors by default for consistent emergency contrast
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AarogyaRakshaTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
