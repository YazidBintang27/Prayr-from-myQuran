package com.muslim.prayr.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.muslim.prayr.data.repository.Repository
import com.muslim.prayr.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class SplashViewModel @Inject constructor(
   private val repository: Repository,
   private val fusedLocationProviderClient: FusedLocationProviderClient,
   @ApplicationContext private val context: Context
) : ViewModel() {

   private var _locationState = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
   val locationState: StateFlow<ResultState<Unit>> = _locationState.asStateFlow()

   private val _location = MutableStateFlow<Location?>(null)
   val location: StateFlow<Location?> = _location.asStateFlow()

   private val _cityName = MutableStateFlow<String>("Unknown")
   val cityName: StateFlow<String> = _cityName.asStateFlow()


   private val _cityId = MutableStateFlow<String>("")
   val cityId: StateFlow<String> = _cityId.asStateFlow()

   init {
      _locationState.value = ResultState.Loading
   }

   @SuppressLint("MissingPermission")
   fun fetchLastLocation() {
      viewModelScope.launch {
         try {
            val lastLocation = getLastLocation()
            _location.value = lastLocation

            lastLocation?.let {
               val locality = getCityName(it.latitude, it.longitude)
               _cityName.value = locality
               getLocationId()
            }
         } catch (e: Exception) {
            Log.e("LocationViewModel", "Error fetching location: ${e.message}")
         }
      }
   }

   @SuppressLint("MissingPermission")
   private suspend fun getLastLocation(): Location? = suspendCancellableCoroutine { cont ->
      fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
         cont.resume(location)
      }.addOnFailureListener { e ->
         Log.e("LocationViewModel", "Error fetching last location: ${e.message}")
         cont.resume(null)
      }
   }

   private fun getCityName(latitude: Double, longitude: Double): String {
      return try {
         val geocoder = Geocoder(context, Locale.getDefault())
         val address = geocoder.getFromLocation(latitude, longitude, 1)
            ?.firstOrNull()

         address?.subAdminArea
            ?: address?.locality
            ?: "Unknown"

      } catch (e: Exception) {
         Log.e("LocationViewModel", "Geocoder failed: ${e.message}")
         "Unknown"
      }
   }

   private fun normalizeCityName(name: String): String {
      val cleanName = name.trim()

      return when {
         cleanName.contains("Regency", ignoreCase = true) -> {
            val base = cleanName.replace("Regency", "", ignoreCase = true).trim()
            "KAB. $base"
         }

         cleanName.contains("City", ignoreCase = true) -> {
            val base = cleanName.replace("City", "", ignoreCase = true).trim()
            "KOTA $base"
         }

         cleanName.contains("Kabupaten", ignoreCase = true) -> {
            val base = cleanName.replace("Kabupaten", "", ignoreCase = true).trim()
            "KAB. $base"
         }

         cleanName.contains("Kota", ignoreCase = true) -> {
            val base = cleanName.replace("Kota", "", ignoreCase = true).trim()
            "KOTA $base"
         }

         else -> cleanName
      }
   }

   fun getLocationId() {
      viewModelScope.launch {
         try {

            val cityQuery = normalizeCityName(_cityName.value)

            if (cityQuery.isBlank() || cityQuery == "Unknown") {
               _locationState.value = ResultState.Error("City not valid")
               return@launch
            }

            val response = repository.getAllCity(cityQuery)

            _cityName.value = cityQuery

            val cityId = response.data
               ?.firstOrNull()
               ?.id

            if (cityId != null) {
               _cityId.value = cityId
               _locationState.value = ResultState.Success(Unit)
            } else {
               _locationState.value =
                  ResultState.Error("City ID not found for $cityQuery")
            }

         } catch (e: Exception) {
            _locationState.value =
               ResultState.Error(e.message ?: "Unknown error")
         }
      }
   }
}