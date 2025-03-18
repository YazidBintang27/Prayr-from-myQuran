package com.muslim.prayr.data.remote.services

import com.muslim.prayr.data.remote.models.AllCity
import com.muslim.prayr.data.remote.models.HijriCalendar
import com.muslim.prayr.data.remote.models.SalahSchedule
import com.muslim.prayr.utils.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
   @GET(ApiConstant.ALL_CITY)
   suspend fun getAllCity(): AllCity

   @GET(ApiConstant.SALAH_DAILY_SCHEDULE)
   suspend fun getDailySchedule(
      @Path("kota") cityId: Int,
      @Path("date") date: String
   ): SalahSchedule

   @GET(ApiConstant.HIJRI_CALENDAR)
   suspend fun getHijriCalendarToday(): HijriCalendar
}