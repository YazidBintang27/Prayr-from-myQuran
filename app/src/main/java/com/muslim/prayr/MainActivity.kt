package com.muslim.prayr

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.muslim.prayr.ui.pages.Home
import com.muslim.prayr.ui.pages.Splash
import com.muslim.prayr.ui.theme.PrayrTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         PrayrTheme {
            PrayrApp()
         }
      }
   }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun PrayrApp(
   modifier: Modifier = Modifier,
   navHostController: NavHostController = rememberNavController()
) {
   val navBackStackEntry by navHostController.currentBackStackEntryAsState()

   NavHost(
      navController = navHostController,
      startDestination = Screen.Splash.route,
      modifier = Modifier
   ) {
      composable(Screen.Splash.route) {
         Splash(navHostController = navHostController)
      }
      composable(
         route = Screen.Home.route,
         arguments = listOf(
            navArgument("id") { type = NavType.StringType },
            navArgument("cityName") { type = NavType.StringType }
         )
      ) {
         val id = it.arguments?.getString("id") ?: ""
         val cityName = it.arguments?.getString("cityName") ?: ""
         Home(id = id, navHostController = navHostController, cityName = cityName)
      }
   }
}

sealed class Screen(val route: String) {
   data object Splash: Screen("splash")
   data object Home: Screen("home/{id}/{cityName}") {
      fun createRoute(id: String, cityName: String): String {
         val encodedCity = Uri.encode(cityName)
         return "home/$id/$encodedCity"
      }
   }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
   PrayrTheme {
      PrayrApp()
   }
}