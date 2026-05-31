package com.example.hw_01_sem2.ui.screen.about

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.api.AboutAppAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutAppViewModel @Inject constructor(
    private val aboutAnalytics: AboutAppAnalytics,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_HAS_SEEN_ABOUT = "has_seen_about"
        private const val ANALYTICS_ACTION_ACCEPT = "accept_about_screen"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _showDialog = MutableStateFlow(!prefs.getBoolean(KEY_HAS_SEEN_ABOUT, false))
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun onSheetOpened() {
        aboutAnalytics.trackAboutAppOpened()
    }

    fun onAcceptClicked() {
        prefs.edit().putBoolean(KEY_HAS_SEEN_ABOUT, true).apply()
        _showDialog.value = false
        aboutAnalytics.trackActionClicked(ANALYTICS_ACTION_ACCEPT)
    }
}