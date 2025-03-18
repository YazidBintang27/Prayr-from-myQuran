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

   private val _regionName = MutableStateFlow<String>("Unknown")

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
               val (locality, subAdminArea) = getCityRegionInfo(it.latitude, it.longitude)
               _cityName.value = locality
               _regionName.value = subAdminArea
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
         val addresses = geocoder.getFromLocation(latitude, longitude, 1)
         addresses?.firstOrNull()?.locality ?: "Unknown"
      } catch (e: Exception) {
         Log.e("LocationViewModel", "Geocoder failed: ${e.message}")
         "Unknown"
      }
   }

   private fun getCityRegionInfo(latitude: Double, longitude: Double): Pair<String, String> {
      return try {
         val geocoder = Geocoder(context, Locale.getDefault())
         val addresses = geocoder.getFromLocation(latitude, longitude, 1)
         val address = addresses?.firstOrNull()

         val city = address?.subAdminArea ?: "Unknown"
         val country = address?.countryName ?: "Unknown"

         val formattedLocation = "$city, $country"

         Pair(formattedLocation, address?.subAdminArea ?: "Unknown")
      } catch (e: Exception) {
         Log.e("LocationViewModel", "Geocoder failed: ${e.message}")
         Pair("Unknown", "Unknown")
      }
   }

   fun getLocationId() {
      viewModelScope.launch {
         try {
            val response = repository.getAllCity()

            val isKecamatan = _cityName.value.startsWith("Kecamatan", ignoreCase = true)

            var cityData = response.data?.find { cityItem ->
               val apiCityName = cityItem?.lokasi ?: ""

               if (_regionName.value.contains("Tangerang", ignoreCase = true)) {
                  apiCityName.contains("TANGERANG", ignoreCase = true) &&
                        !apiCityName.contains("SELATAN", ignoreCase = true) &&
                        !apiCityName.contains("KOTA", ignoreCase = true)
               } else {
                  // Normal matching logic for other regions
                  val cleanApiName = apiCityName
                     .replace("KAB. ", "")
                     .replace("KABUPATEN ", "")
                     .replace("KOTA ", "")

                  apiCityName.contains(_regionName.value, ignoreCase = true) ||
                        cleanApiName.equals(_regionName.value, ignoreCase = true) ||
                        _regionName.value.contains(cleanApiName, ignoreCase = true)
               }
            }

            if (cityData == null) {
               val mainRegionName = _regionName.value
                  .replace("Kabupaten ", "", ignoreCase = true)
                  .replace("Kota ", "", ignoreCase = true)

               cityData = response.data?.find { cityItem ->
                  (cityItem?.lokasi ?: "").contains(mainRegionName, ignoreCase = true)
               }
            }

            _cityId.value = cityData?.id ?: ""

            if (cityData != null) {
               Log.d("SplashViewModel", "City ID found: ${cityData.id} for location: ${cityData.lokasi}")
            } else {
               Log.d("SplashViewModel", "City ID not found for ${if (isKecamatan) "kecamatan" else "locality"}: ${_cityName.value}, subAdminArea: ${_regionName.value}")
            }

            _locationState.value = ResultState.Success(Unit)
         } catch (e: Exception) {
            Log.e("SplashViewModel", "Error getting location ID: ${e.message}")
            _locationState.value = ResultState.Error(e.message ?: "Unknown error")
         }
      }
   }
}