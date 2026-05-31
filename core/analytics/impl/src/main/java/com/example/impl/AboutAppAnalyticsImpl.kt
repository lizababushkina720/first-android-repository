package com.example.impl

import com.example.api.AboutAppAnalytics
import com.example.api.Analytics
import javax.inject.Inject

class AboutAppAnalyticsImpl @Inject constructor(
    private val analytics: Analytics
) : AboutAppAnalytics {

    companion object {
        private const val EVENT_ABOUT_OPENED = "about_app_opened"
        private const val EVENT_ACTION_CLICKED = "about_app_action_clicked"

        private const val PARAM_SCREEN_TYPE = "screen_type"
        private const val PARAM_ACTION_NAME = "action_name"
        private const val VALUE_BOTTOM_SHEET = "bottom_sheet"
    }

    override fun trackAboutAppOpened() {
        analytics.trackEvent(
            eventName = EVENT_ABOUT_OPENED,
            PARAM_SCREEN_TYPE to VALUE_BOTTOM_SHEET
        )
    }

    override fun trackActionClicked(actionName: String) {
        analytics.trackEvent(
            eventName = EVENT_ACTION_CLICKED,
            PARAM_ACTION_NAME to actionName
        )
    }
}