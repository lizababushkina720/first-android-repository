package com.example.hw_02.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hw_02.models.Note
import com.example.hw_02.ui.screens.AddNoteScreen
import com.example.hw_02.ui.screens.LoginScreen
import com.example.hw_02.ui.screens.NotesScreen
import com.example.hw_02.ui.theme.ThemeType
import com.example.hw_02.utils.Constants

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    onThemeChange: (ThemeType) -> Unit
) {
    val notes = remember { mutableStateListOf<Note>() }
    var currentTheme by remember { mutableStateOf(ThemeType.DEFAULT) }

    NavHost(
        navController = navController,
        startDestination = Constants.Routes.LOGIN
    ) {
        composable(Constants.Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { email ->
                    navController.navigate(Constants.Routes.notesRoute(email)) {
                        popUpTo(Constants.Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Constants.Routes.NOTES) { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString(Constants.ARG_EMAIL) ?: ""

            NotesScreen(
                userEmail = userEmail,
                notes = notes,
                onAddNoteClick = {
                    navController.navigate(Constants.Routes.ADD_NOTE)
                },
                currentTheme = currentTheme,
                onThemeChange = { newTheme ->
                    currentTheme = newTheme
                    onThemeChange(newTheme)
                }
            )
        }
        composable(Constants.Routes.ADD_NOTE) {
            AddNoteScreen(
                onSaveNote = { title, content ->
                    notes.add(Note(title = title, content = content))
                    navController.popBackStack()
                }
            )
        }
    }
}
