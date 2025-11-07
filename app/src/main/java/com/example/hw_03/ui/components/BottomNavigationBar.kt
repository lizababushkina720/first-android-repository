package com.example.hw_03.ui.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.hw_03.R
import com.example.hw_03.ui.navigation.Screen


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            screen = Screen.NotificationSettings,
            icon = Icons.Default.Settings,
            label = stringResource(id = R.string.nav_settings)
        ),
        BottomNavItem(
            screen = Screen.NotificationEdit,
            icon = Icons.Default.Edit,
            label = stringResource(id = R.string.nav_edit)
        ),
        BottomNavItem(
            screen = Screen.Messages,
            icon = Icons.Default.Message,
            label = stringResource(id = R.string.nav_messages)
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


private data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)
