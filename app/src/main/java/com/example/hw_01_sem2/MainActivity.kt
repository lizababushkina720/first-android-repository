package com.example.hw_01_sem2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.api.Analytics
import com.example.hw_01_sem2.fcm.AppMessagingService
import com.example.hw_01_sem2.navigation.AppNavHost
import com.example.hw_01_sem2.ui.screen.search.SearchViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val CRASHLYTICS_KEY_USER_ID = "userId"
    }

    @Inject
    lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupCrashlytics()
        val startOrganizationInn = intent.getStringExtra(AppMessagingService.EXTRA_ORGANIZATION_INN)

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        analytics = analytics,
                        startOrganizationInn = startOrganizationInn
                    )
                }
            }
        }

    }
    private fun setupCrashlytics() {
        val userId = UUID.randomUUID().toString()
        FirebaseCrashlytics.getInstance().setCustomKey(CRASHLYTICS_KEY_USER_ID, userId)
    }
}