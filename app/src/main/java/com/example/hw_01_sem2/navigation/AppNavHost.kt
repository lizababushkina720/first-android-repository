package com.example.hw_01_sem2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.domain.model.OrganizationModel
import com.example.hw_01_sem2.ui.screen.detail.DetailScreen
import com.example.hw_01_sem2.ui.screen.search.SearchScreen
import com.example.hw_01_sem2.ui.screen.search.SearchViewModel
import com.google.gson.Gson

private val gson = Gson()

@Composable
fun AppNavHost(
    navController: NavHostController,
    searchViewModel: SearchViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        composable(Routes.SEARCH) {
            SearchScreen(
                viewModel = searchViewModel,
                onOrganizationClick = { organization ->
                    val json = gson.toJson(organization)
                    navController.navigate(Routes.detailRoute(json))
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("organizationJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("organizationJson")
                ?.let { java.net.URLDecoder.decode(it, "UTF-8") }
                ?: return@composable

            val organization = gson.fromJson(json, OrganizationModel::class.java)

           DetailScreen(
                organization = organization,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}