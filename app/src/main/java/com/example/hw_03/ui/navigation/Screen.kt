package com.example.hw_03.ui.navigation


import com.example.hw_03.R


sealed class Screen(val route: String, val titleResId: Int) {
    object NotificationSettings : Screen(
        route = "notification_settings",
        titleResId = R.string.nav_settings
    )

    object NotificationEdit : Screen(
        route = "notification_edit",
        titleResId = R.string.nav_edit
    )

    object Messages : Screen(
        route = "messages",
        titleResId = R.string.nav_messages
    )
}
