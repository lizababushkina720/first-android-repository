package com.example.impl

import com.example.api.BuildConfigProvider

class BuildConfigProviderImpl: BuildConfigProvider {
    override fun getDaDataApiBaseUrl(): String =BuildConfig.DADATA_API_BASE_URL

    override fun getDaDataApiKey(): String = BuildConfig.DADATA_API_KEY

}