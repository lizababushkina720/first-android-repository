package com.example.api


interface Analytics {
    fun trackEvent(eventName: String, vararg params: Pair<String, Any>)
}