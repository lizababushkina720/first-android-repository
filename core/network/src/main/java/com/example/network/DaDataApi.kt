package com.example.network

import retrofit2.http.Body
import retrofit2.http.POST
import com.example.network.pojo.response.DaDataResponse
import com.example.network.pojo.request.DaDataRequest

interface DaDataApi {

    @POST("suggest/party")
    suspend fun suggestParty(
        @Body request: DaDataRequest,
    ): DaDataResponse


}