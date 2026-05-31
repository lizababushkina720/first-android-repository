package com.example.api


interface OrganizationRequestAnalytics {
    fun trackSearchOrganizationEvent(query: String)

    fun trackSearchDataSourceEvent(source: String)
}