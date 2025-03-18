package com.muslim.prayr.data.remote.models


import com.google.gson.annotations.SerializedName

data class AllCity(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("request")
    val request: Request?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("id")
        val id: String?,
        @SerializedName("lokasi")
        val lokasi: String?
    )

    data class Request(
        @SerializedName("path")
        val path: String?
    )
}