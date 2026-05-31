package com.example.network.pojo.request


import com.google.gson.annotations.SerializedName

data class DaDataRequest(
    @SerializedName("query")
    val query: String,

    @SerializedName("count")
    val count: Int? = 20,
)