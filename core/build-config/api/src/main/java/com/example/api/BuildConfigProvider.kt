package com.example.api

interface BuildConfigProvider {
    fun getDaDataApiBaseUrl(): String

    fun getDaDataApiKey(): String
}