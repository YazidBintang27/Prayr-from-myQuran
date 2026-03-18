package com.muslim.prayr.ui.pages

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.muslim.prayr.R
import com.muslim.prayr.Screen
import com.muslim.prayr.ui.theme.AppBackground
import com.muslim.prayr.ui.theme.PrayrTheme
import com.muslim.prayr.ui.viewmodels.SplashViewModel
import com.muslim.prayr.utils.ResultState

@Composable
fun Splash(
   modifier: Modifier = Modifier,
   navHostController: NavHostController,
   viewModel: SplashViewModel = hiltViewModel()
) {

   val context = LocalContext.current
   val locationState by viewModel.locationState.collectAsState()
   val cityId by viewModel.cityId.collectAsState()
   val cityName by viewModel.cityName.collectAsState()

   val permissionLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestPermission()
   ) { isGranted ->
      if (isGranted) {
         viewModel.fetchLastLocation()
      } else {
         Toast.makeText(context, "Izin lokasi diperlukan", Toast.LENGTH_SHORT).show()
      }
   }

   LaunchedEffect(Unit) {
      if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
         ) == PackageManager.PERMISSION_GRANTED
      ) {
         viewModel.fetchLastLocation()
      } else {
         permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
      }
   }

   when (locationState) {
      is ResultState.Idle -> {}
      is ResultState.Loading -> { SplashContent(viewModel = viewModel) }
      is ResultState.Success -> {
         LaunchedEffect(Unit) {
            navHostController.navigate(Screen.Home.createRoute(cityId, cityName)) {
               popUpTo(Screen.Splash.route) { inclusive = true }
            }
         }
      }
      is ResultState.Error -> { Toast.makeText(context, cityName, Toast.LENGTH_SHORT).show() }
   }
}

@Composable
fun SplashContent(viewModel: SplashViewModel) {
   Box(
      modifier = Modifier
         .fillMaxSize()
         .background(color = AppBackground),
      contentAlignment = Alignment.Center
   ) {
      Image(
         painter = painterResource(R.drawable.app_logo_new_prayr),
         contentDescription = "App Logo",
         modifier = Modifier.size(120.dp)
      )
   }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashContentPreview() {
   PrayrTheme {
//      SplashContent()
   }
}
