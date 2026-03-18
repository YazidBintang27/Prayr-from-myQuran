package com.muslim.prayr.utils

sealed class ResultState<out T> {
   data object Idle : ResultState<Nothing>()
   data object Loading : ResultState<Nothing>()
   data class Success<T>(val data: T, val successMessage: String? = null) : ResultState<T>()
   data class Error(val errorMessage: String) : ResultState<Nothing>()
}