package com.example.impl

import com.example.api.Analytics
import com.example.api.OrganizationRequestAnalytics
import javax.inject.Inject

class OrganizationRequestAnalyticsImpl @Inject constructor(
    private val analytics: Analytics
) : OrganizationRequestAnalytics {

    companion object {
        private const val EVENT_SEARCH_ORGANIZATION = "search_organization"
        private const val EVENT_DATA_SOURCE = "organization_data_source"
        private const val PARAM_SEARCH_QUERY = "search_query"
        private const val PARAM_SOURCE_TYPE = "source_type"
    }

    override fun trackSearchOrganizationEvent(query: String) {
        analytics.trackEvent(
            eventName = EVENT_SEARCH_ORGANIZATION,
            PARAM_SEARCH_QUERY to query
        )
    }

    override fun trackSearchDataSourceEvent(source: String) {
        analytics.trackEvent(
            eventName = EVENT_DATA_SOURCE,
            PARAM_SOURCE_TYPE to source
        )
    }
}