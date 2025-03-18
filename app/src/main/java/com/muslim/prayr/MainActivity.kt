package com.muslim.prayr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

@Composable
fun PrayrApp(
   modifier: Modifier = Modifier,
   navHostController: NavHostController = rememberNavController()
) {
   val navBackStackEntry by navHostController.currentBackStackEntryAsState()
   val currentRoute = navBackStackEntry?.destination?.route

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
      fun createRoute(id: String, cityName: String) = "home/$id/$cityName"
   }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
   PrayrTheme {
      PrayrApp()
   }
}