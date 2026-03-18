package com.muslim.prayr.ui.pages

import android.annotation.SuppressLint
import android.content.Context
import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
   val scheduleState by viewModel.scheduleState.collectAsState()
   val hijriState by viewModel.hijriState.collectAsState()

   LaunchedEffect(Unit) {
      if (id.isNotBlank()) {
         viewModel.loadHomeData(id)
      }
   }

   when {
      scheduleState is ResultState.Idle || hijriState is ResultState.Idle -> {
      }
      scheduleState is ResultState.Loading || hijriState is ResultState.Loading -> {
         Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
         ) {
            CircularProgressIndicator()
         }
      }

      scheduleState is ResultState.Error -> {
         Toast.makeText(context, "Failed to load prayer schedule", Toast.LENGTH_SHORT).show()
      }

      hijriState is ResultState.Error -> {
         Toast.makeText(context, "Failed to load hijri calendar", Toast.LENGTH_SHORT).show()
      }

      scheduleState is ResultState.Success &&
            hijriState is ResultState.Success -> {

         val schedule =
            (scheduleState as ResultState.Success<SalahSchedule>).data
         val hijri =
            (hijriState as ResultState.Success<HijriCalendar>).data

         HomeContent(
            context = context,
            viewModel = viewModel,
            scheduleData = schedule,
            hijriCalendar = hijri,
            cityName = cityName
         )
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
            colors = TopAppBarDefaults.topAppBarColors(
               containerColor = MaterialTheme.colorScheme.background,
               titleContentColor = MaterialTheme.colorScheme.background
            ),
            title = {
               Row(
                  modifier = Modifier
                     .fillMaxSize(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Start
               ) {
                  Image(
                     painter = painterResource(R.drawable.app_logo_new_prayr),
                     contentScale = ContentScale.Fit,
                     contentDescription = "App Logo",
                     modifier = Modifier.size(80.dp)
                  )
               }
            },
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
               .border(width = 1.dp, color = Color(0x80D1D1D1))
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
                  style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
               )
               Spacer(modifier = Modifier.height(12.dp))
               Text(
                  "${hijriCalendar?.data?.date?.get(2)?.let { formatDate(it) }}",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
               )
               Row(
                  modifier = Modifier.fillMaxHeight(),
                  verticalAlignment = Alignment.Bottom
               ) {
                  Text(
                     "$cityName, ID",
                     style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
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
            "Jadwal Sholat",
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
