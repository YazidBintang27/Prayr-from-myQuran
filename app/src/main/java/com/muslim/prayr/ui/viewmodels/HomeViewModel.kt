package com.muslim.prayr.ui.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslim.prayr.data.remote.models.HijriCalendar
import com.muslim.prayr.data.remote.models.SalahSchedule
import com.muslim.prayr.data.repository.Repository
import com.muslim.prayr.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {
   private val _scheduleState = MutableStateFlow<ResultState<SalahSchedule>>(ResultState.Idle)
   val scheduleState = _scheduleState.asStateFlow()

   private val _hijriState = MutableStateFlow<ResultState<HijriCalendar>>(ResultState.Idle)
   val hijriState: StateFlow<ResultState<HijriCalendar>> = _hijriState.asStateFlow()

   private var _date = MutableStateFlow<String>("")
   private val date: StateFlow<String> = _date.asStateFlow()

   init {
      getDate()
   }

   private var hasLoaded = false

   fun loadHomeData(cityId: String) {
      if (hasLoaded) return
      hasLoaded = true

      getDailySchedule(cityId)
      getHijriCalendarToday()
   }

   @SuppressLint("NewApi")
   private fun getDate() {
      val today = LocalDate.now()
      val formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      _date.value = formattedDate
   }

   private fun getDailySchedule(cityId: String) {
      viewModelScope.launch {
         _scheduleState.value = ResultState.Loading
         try {
            val response = repository.getDailySchedule(cityId.toInt(), date.value)
            _scheduleState.value = ResultState.Success(response)
         } catch (e: Exception) {
            Log.e("HomeViewModel", "${e.message}")
            _scheduleState.value = ResultState.Error(e.message ?: "Unknown error")
         }
      }
   }

   private fun getHijriCalendarToday() {
      viewModelScope.launch {
         _hijriState.value = ResultState.Loading
         try {
            val response = repository.getHijriCalendarToday()
            _hijriState.value = ResultState.Success(response)
         } catch (e: Exception) {
            Log.e("HomeViewModel", "${e.message}")
            _hijriState.value = ResultState.Error(e.message ?: "Unknown error")
         }
      }
   }
}