package com.muslim.prayr.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muslim.prayr.R
import com.muslim.prayr.ui.components.ScheduleCard
import com.muslim.prayr.ui.theme.PrayrTheme

@Composable
fun Home(modifier: Modifier = Modifier) {
   HomeContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent() {
   Scaffold(
      topBar = {
         TopAppBar(
            title = {
               Row(
                  modifier = Modifier
                     .fillMaxSize()
                     .padding(end = 40.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center
               ) {
                  Image(
                     painter = painterResource(R.drawable.app_logo),
                     contentScale = ContentScale.Fit,
                     contentDescription = "App Logo",
                     modifier = Modifier.size(60.dp)
                  )
               }
            },
            navigationIcon = {
               IconButton(
                  onClick = {}
               ) {
                  Icon(
                     painter = painterResource(R.drawable.menu_02_stroke_rounded),
                     contentDescription = "Menu Navigation Icon"
                  )
               }
            }
         )
      }
   ) { innerPadding ->
      Column(
         modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
      ) {
         Box(
            modifier = Modifier
               .padding(top = 12.dp, bottom = 24.dp)
               .height(200.dp)
               .clip(shape = RoundedCornerShape(28.dp))
         ) {
            Image(
               painter = painterResource(R.drawable.card_background),
               contentScale = ContentScale.Crop,
               contentDescription = "Hero Image"
            )
            Column(
               modifier = Modifier
                  .fillMaxSize()
                  .padding(20.dp),
               horizontalAlignment = Alignment.End
            ) {
               Spacer(modifier = Modifier.height(40.dp))
               Text(
                  "16 Ramadhan 1446 H",
                  style = MaterialTheme.typography.bodyLarge,
               )
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                  "16 Maret 2025",
                  style = MaterialTheme.typography.bodySmall,
               )
               Row(
                  modifier = Modifier.fillMaxHeight(),
                  verticalAlignment = Alignment.Bottom
               ) {
                  Text(
                     "Tangerang, Indonesia",
                     style = MaterialTheme.typography.bodySmall,
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Icon(
                     painter = painterResource(R.drawable.location_01_stroke_rounded),
                     contentDescription = "Location Icon",
                     modifier = Modifier.size(14.dp)
                  )
               }
            }
         }
         Text(
            "Prayer Schedule",
            style = MaterialTheme.typography.bodyLarge
         )
         Spacer(modifier = Modifier.height(14.dp))
         LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
         ) {
            items(8) {
               ScheduleCard()
            }
         }
      }
   }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
   PrayrTheme {
      HomeContent()
   }
}
