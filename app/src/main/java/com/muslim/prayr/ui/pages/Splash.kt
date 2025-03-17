package com.muslim.prayr.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muslim.prayr.R
import com.muslim.prayr.ui.theme.AppBackground
import com.muslim.prayr.ui.theme.PrayrTheme

@Composable
fun Splash(modifier: Modifier = Modifier) {
   SplashContent()
}

@Composable
fun SplashContent() {
   Box(
      modifier = Modifier
         .fillMaxSize()
         .background(color = AppBackground),
      contentAlignment = Alignment.Center
   ) {
      Image(
         painter = painterResource(R.drawable.app_logo),
         contentDescription = "App Logo",
         modifier = Modifier.size(80.dp)
      )
   }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashContentPreview() {
   PrayrTheme {
      SplashContent()
   }
}
