package com.example.hw_01_sem2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.di.ServiceLocator
import com.example.hw_01_sem2.navigation.AppNavHost
import com.example.hw_01_sem2.ui.screen.search.SearchViewModel

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by lazy {
        SearchViewModel(
            getOrganizationsByQueryUseCase = ServiceLocator.getGetOrganizationsByQueryUseCase()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()

                    AppNavHost(
                        navController = navController,
                        searchViewModel = searchViewModel
                    )
                }
            }
        }
    }
}