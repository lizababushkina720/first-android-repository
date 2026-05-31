package com.example.hw_01_sem2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.api.Analytics
import com.example.hw_01_sem2.ui.screen.about.AboutAppViewModel
import com.example.hw_01_sem2.ui.screen.detail.DetailScreen
import com.example.hw_01_sem2.ui.screen.detail.DetailViewModel
import com.example.hw_01_sem2.ui.screen.search.SearchScreen
import com.example.hw_01_sem2.ui.screen.search.SearchViewModel
import com.google.firebase.analytics.FirebaseAnalytics

private const val DEFAULT_SCREEN_NAME = "unknown"

@Composable
fun AppNavHost(
    navController: NavHostController,
    analytics: Analytics,
    startOrganizationInn: String? = null
) {
    LaunchedEffect(startOrganizationInn) {
        if (startOrganizationInn != null) {
            navController.navigate(Routes.detailRoute(startOrganizationInn)) {
                popUpTo(Routes.SEARCH) { saveState = true }
                launchSingleTop = true
            }
        }
    }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val screenName = destination.route ?: DEFAULT_SCREEN_NAME
            analytics.trackEvent(
                eventName = FirebaseAnalytics.Event.SCREEN_VIEW,
                FirebaseAnalytics.Param.SCREEN_NAME to screenName,
                FirebaseAnalytics.Param.SCREEN_CLASS to screenName
            )
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        composable(Routes.SEARCH) {
            val viewModel: SearchViewModel = hiltViewModel()
            val aboutViewModel: AboutAppViewModel = hiltViewModel()
            SearchScreen(
                viewModel = viewModel,
                aboutViewModel = aboutViewModel,
                onOrganizationClick = { organization ->
                    navController.navigate(Routes.detailRoute(organization.inn))
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument(Routes.ARG_ORGANIZATION_INN) { type = NavType.StringType }
            )
        ) {
            val viewModel: DetailViewModel = hiltViewModel()
            DetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}