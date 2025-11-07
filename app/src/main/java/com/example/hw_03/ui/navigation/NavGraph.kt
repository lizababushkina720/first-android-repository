package com.example.hw_03.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hw_03.ui.screens.MessagesScreen
import com.example.hw_03.ui.screens.NotificationEditScreen
import com.example.hw_03.ui.screens.NotificationSettingsScreen
import com.example.hw_03.utils.NotificationHandler


@Composable
fun NavGraph(
    navController: NavHostController,
    notificationHandler: NotificationHandler,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NotificationSettings.route,
        modifier = modifier
    ) {
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(notificationHandler = notificationHandler)
        }
        composable(Screen.NotificationEdit.route) {
            NotificationEditScreen(notificationHandler = notificationHandler)
        }
        composable(Screen.Messages.route) {
            MessagesScreen()
        }
    }
}
