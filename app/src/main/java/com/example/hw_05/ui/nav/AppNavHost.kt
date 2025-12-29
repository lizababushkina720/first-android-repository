package com.example.hw_05.ui.nav

import com.example.hw_05.data.prefs.SessionStore
import com.example.hw_05.ui.auth.AuthScreen
import com.example.hw_05.ui.auth.RecoveryScreen
import com.example.hw_05.ui.auth.RegisterScreen
import com.example.hw_05.ui.pets.AddPetScreen
import com.example.hw_05.ui.pets.PetsListScreen
import com.example.hw_05.ui.profile.ProfileScreen


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.hw_05.ui.auth.RegisterOkScreen
import com.example.hw_05.ui.pets.AddPetOkScreen


@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val startDestination = remember {
        val userId = SessionStore().getCurrentUserId()
        if (userId != null) Routes.PETS else Routes.AUTH
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.AUTH) { AuthScreen(navController) }
        composable(Routes.REGISTER) { RegisterScreen(navController) }
        composable(
            route = Routes.RECOVERY_WITH_ID,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("id") ?: 0L
            RecoveryScreen(
                navController = navController,
                userId = userId
            )
        }

        composable(Routes.REGISTER_OK) { RegisterOkScreen(navController) }
        composable(Routes.ADD_PET_OK) { AddPetOkScreen(navController) }
        composable(Routes.PETS) { PetsListScreen(navController) }
        composable(Routes.ADD_PET) { AddPetScreen(navController) }
        composable(Routes.PROFILE) { ProfileScreen(navController) }

    }
}
