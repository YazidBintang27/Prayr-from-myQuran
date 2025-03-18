package com.muslim.prayr.ui.pages

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.muslim.prayr.R
import com.muslim.prayr.data.remote.models.HijriCalendar
import com.muslim.prayr.data.remote.models.SalahSchedule
import com.muslim.prayr.ui.components.ScheduleCard
import com.muslim.prayr.ui.theme.PrayrTheme
import com.muslim.prayr.ui.viewmodels.HomeViewModel
import com.muslim.prayr.utils.ResultState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun Home(
   modifier: Modifier = Modifier,
   navHostController: NavHostController,
   id: String,
   cityName: String,
   viewModel: HomeViewModel = hiltViewModel()
) {
   val context = LocalContext.current
   val homeState by viewModel.homeState.collectAsState()
   val scheduleData by viewModel.scheduleData.collectAsState()
   val hijriCalendar by viewModel.hijriCalendarToday.collectAsState()

   LaunchedEffect(Unit) {
      Log.d("HomePage", "ID: $id")
      viewModel.getDailySchedule(id)
   }

   when (homeState) {
      is ResultState.Loading -> {
         Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
         ) {
            CircularProgressIndicator()
         }
      }

      is ResultState.Success -> {
         HomeContent(
            context = context, viewModel = viewModel, scheduleData = scheduleData,
            hijriCalendar = hijriCalendar, cityName = cityName
         )
      }

      is ResultState.Error -> {
         Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
      }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
   context: Context,
   viewModel: HomeViewModel,
   scheduleData: SalahSchedule?,
   hijriCalendar: HijriCalendar?,
   cityName: String
) {

   val prayerTimes = if (scheduleData != null) {
      listOf(
         "Imsak" to scheduleData.data?.jadwal?.imsak,
         "Subuh" to scheduleData.data?.jadwal?.subuh,
         "Terbit" to scheduleData.data?.jadwal?.terbit,
         "Dhuha" to scheduleData.data?.jadwal?.dhuha,
         "Dzuhur" to scheduleData.data?.jadwal?.dzuhur,
         "Ashar" to scheduleData.data?.jadwal?.ashar,
         "Maghrib" to scheduleData.data?.jadwal?.maghrib,
         "Isya" to scheduleData.data?.jadwal?.isya
      )
   } else {
      listOf()
   }

   Scaffold(
      containerColor = MaterialTheme.colorScheme.background,
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
                  onClick = {
                     Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
                  }
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
                  "${hijriCalendar?.data?.date?.get(1)}",
                  style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
               )
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                  "${hijriCalendar?.data?.date?.get(2)?.let { formatDate(it) }}",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
               )
               Row(
                  modifier = Modifier.fillMaxHeight(),
                  verticalAlignment = Alignment.Bottom
               ) {
                  Text(
                     cityName,
                     style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Icon(
                     painter = painterResource(R.drawable.location_01_stroke_rounded),
                     contentDescription = "Location Icon",
                     modifier = Modifier.size(16.dp)
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
            items(prayerTimes.size) { index ->
               val (name, time) = prayerTimes[index]
               ScheduleCard(prayerName = name, prayerTime = time)
            }
         }
      }
   }
}

@SuppressLint("NewApi")
fun formatDate(input: String): String {
   val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
   val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

   val date = inputFormat.parse(input)
   return outputFormat.format(date!!)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
   PrayrTheme {
//      HomeContent()
   }
}
