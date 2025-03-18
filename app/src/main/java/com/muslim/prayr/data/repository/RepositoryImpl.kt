package com.muslim.prayr.data.repository

import com.muslim.prayr.data.remote.models.AllCity
import com.muslim.prayr.data.remote.models.HijriCalendar
import com.muslim.prayr.data.remote.models.SalahSchedule
import com.muslim.prayr.data.remote.services.ApiService
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
   private val apiService: ApiService
) : Repository {
   override suspend fun getAllCity(): AllCity {
      return apiService.getAllCity()
   }

   override suspend fun getDailySchedule(cityId: Int, date: String): SalahSchedule {
      return apiService.getDailySchedule(cityId, date)
   }

   override suspend fun getHijriCalendarToday(): HijriCalendar {
      return apiService.getHijriCalendarToday()
   }
}