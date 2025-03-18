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
   private var _homeState = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
   val homeState: StateFlow<ResultState<Unit>> = _homeState.asStateFlow()

   private var _date = MutableStateFlow<String>("")
   private val date: StateFlow<String> = _date.asStateFlow()

   private var _scheduleData = MutableStateFlow<SalahSchedule?>(null)
   val scheduleData: StateFlow<SalahSchedule?> = _scheduleData.asStateFlow()

   private var _hijriCalendarToday = MutableStateFlow<HijriCalendar?>(null)
   val hijriCalendarToday: StateFlow<HijriCalendar?> = _hijriCalendarToday.asStateFlow()

   init {
      getDate()
      getHijriCalendarToday()
   }

   @SuppressLint("NewApi")
   private fun getDate() {
      val today = LocalDate.now()
      val formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      _date.value = formattedDate
   }

   fun getDailySchedule(cityId: String) {
      viewModelScope.launch {
         _homeState.value = ResultState.Loading
         try {
            val response = repository.getDailySchedule(cityId.toInt(), date.value)
            _scheduleData.value = response
            _homeState.value = ResultState.Success(Unit)
         } catch (e: Exception) {
            Log.e("HomeViewModel", "${e.message}")
         }
      }
   }

   private fun getHijriCalendarToday() {
      viewModelScope.launch {
         _homeState.value = ResultState.Loading
         try {
            val response = repository.getHijriCalendarToday()
            _hijriCalendarToday.value = response
            _homeState.value = ResultState.Success(Unit)
         } catch (e: Exception) {
            Log.e("HomeViewModel", "${e.message}")
         }
      }
   }
}