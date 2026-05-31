package com.example.impl


import android.content.Context
import com.example.api.Analytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : Analytics {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun trackEvent(eventName: String, vararg params: Pair<String, Any>) {
        firebaseAnalytics.logEvent(eventName) {
            params.forEach { (key, value) ->
                when (value) {
                    is String -> param(key, value)
                    is Long -> param(key, value)
                    is Int -> param(key, value.toLong())
                    is Double -> param(key, value)
                    is Boolean -> param(key, value.toString())
                }
            }
        }
    }
}