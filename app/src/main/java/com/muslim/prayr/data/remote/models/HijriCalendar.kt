package com.muslim.prayr.data.remote.models


import com.google.gson.annotations.SerializedName

data class HijriCalendar(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("request")
    val request: Request?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("date")
        val date: List<String?>?,
        @SerializedName("num")
        val num: List<Int?>?
    )

    data class Request(
        @SerializedName("adj")
        val adj: Int?,
        @SerializedName("date")
        val date: String?,
        @SerializedName("path")
        val path: String?
    )
}