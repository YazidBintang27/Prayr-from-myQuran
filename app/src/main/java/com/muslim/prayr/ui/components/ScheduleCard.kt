package com.muslim.prayr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muslim.prayr.R
import com.muslim.prayr.ui.theme.PrayrTheme
import com.muslim.prayr.ui.theme.Primary
import com.muslim.prayr.ui.theme.ScheduleCardColor

@Composable
fun ScheduleCard(modifier: Modifier = Modifier) {
   Box(
      modifier = Modifier
         .height(100.dp)
         .background(color = ScheduleCardColor, shape = RoundedCornerShape(20.dp))
         .border(width = 2.dp, color = Primary, shape = RoundedCornerShape(20.dp))
   ) {
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
      ) {
         Text(
            "Imsak",
            style = MaterialTheme.typography.bodyLarge.copy(
               fontFamily = FontFamily(
                  Font(R.font.awal_ramadhan)
               ), fontSize = 24.sp
            )
         )
         Text(
            "04:30",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp)
         )
      }
   }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleCardPreview() {
   PrayrTheme {
      ScheduleCard()
   }
}