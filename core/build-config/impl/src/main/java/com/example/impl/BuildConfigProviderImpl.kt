package com.example.impl

import com.example.api.BuildConfigProvider
import com.example.buildconfig.impl.BuildConfig
import javax.inject.Inject

class BuildConfigProviderImpl@Inject constructor() : BuildConfigProvider {
    override fun getDaDataApiBaseUrl(): String = BuildConfig.DADATA_API_BASE_URL

    override fun getDaDataApiKey(): String = BuildConfig.DADATA_API_KEY

}