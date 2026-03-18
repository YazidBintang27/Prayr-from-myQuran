package com.muslim.prayr.data.repository

import com.muslim.prayr.data.remote.models.AllCity
import com.muslim.prayr.data.remote.models.HijriCalendar
import com.muslim.prayr.data.remote.models.SalahSchedule

interface Repository {
   suspend fun getAllCity(keyword: String): AllCity
   suspend fun getDailySchedule(cityId: Int, date: String): SalahSchedule
   suspend fun getHijriCalendarToday(): HijriCalendar
}