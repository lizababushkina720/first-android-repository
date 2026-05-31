package com.example.api


interface AboutAppAnalytics {
    fun trackAboutAppOpened()

    fun trackActionClicked(actionName: String)
}