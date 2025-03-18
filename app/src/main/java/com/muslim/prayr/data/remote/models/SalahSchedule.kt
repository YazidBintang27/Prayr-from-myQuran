package com.muslim.prayr.data.remote.models


import com.google.gson.annotations.SerializedName

data class SalahSchedule(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("request")
    val request: Request?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("daerah")
        val daerah: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("jadwal")
        val jadwal: Jadwal?,
        @SerializedName("lokasi")
        val lokasi: String?
    ) {
        data class Jadwal(
            @SerializedName("ashar")
            val ashar: String?,
            @SerializedName("date")
            val date: String?,
            @SerializedName("dhuha")
            val dhuha: String?,
            @SerializedName("dzuhur")
            val dzuhur: String?,
            @SerializedName("imsak")
            val imsak: String?,
            @SerializedName("isya")
            val isya: String?,
            @SerializedName("maghrib")
            val maghrib: String?,
            @SerializedName("subuh")
            val subuh: String?,
            @SerializedName("tanggal")
            val tanggal: String?,
            @SerializedName("terbit")
            val terbit: String?
        )
    }

    data class Request(
        @SerializedName("date")
        val date: String?,
        @SerializedName("month")
        val month: String?,
        @SerializedName("path")
        val path: String?,
        @SerializedName("year")
        val year: String?
    )
}