package com.example.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

import com.example.api.BuildConfigProvider
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(
    private val buildConfigProvider: BuildConfigProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", buildConfigProvider.getDaDataApiKey())
            .build()

        return chain.proceed(newRequest)
    }
}