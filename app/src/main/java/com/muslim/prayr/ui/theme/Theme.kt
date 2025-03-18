package com.muslim.prayr.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
   primary = Primary,
   background = AppBackground,
   onPrimary = AppBackground,
   onBackground = Text
)

private val LightColorScheme = lightColorScheme(
   primary = Primary,
   background = AppBackground,
   onPrimary = AppBackground,
   onBackground = Text
)

@Composable
fun PrayrTheme(
   darkTheme: Boolean = false,
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

   val view = LocalView.current
   if (!view.isInEditMode) {
      SideEffect {
         val window = (view.context as Activity).window

         if (!darkTheme) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
         } else {
            window.decorView.systemUiVisibility = 0
         }
      }
   }

   MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
   )
}