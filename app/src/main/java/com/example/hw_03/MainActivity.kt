package com.example.hw_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.hw_03.ui.theme.HW3Theme
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.hw_03.receiver.NotificationReplyReceiver
import com.example.hw_03.ui.components.BottomNavigationBar
import com.example.hw_03.ui.navigation.NavGraph
import com.example.hw_03.utils.NotificationHandler


class MainActivity : ComponentActivity() {

    private lateinit var notificationHandler: NotificationHandler

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        val messageResId = if (isGranted) {
            R.string.permission_granted
        } else {
            R.string.permission_denied
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkNotificationPermission()

        NotificationReplyReceiver.clearMessages(this)

        handleNotificationIntent()
        notificationHandler = NotificationHandler(this)

        setContent {
            HW3Theme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { paddingValues ->
                    NavGraph(
                        navController = navController,
                        notificationHandler = notificationHandler,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Toast.makeText(
                        this,
                        R.string.permission_rationale,
                        Toast.LENGTH_LONG
                    ).show()
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun handleNotificationIntent() {
        intent?.let { intent ->
            val title = intent.getStringExtra(Keys.EXTRA_NOTIFICATION_TITLE)
            val text = intent.getStringExtra(Keys.EXTRA_NOTIFICATION_TEXT)

            if (title != null || text != null) {
                val message = buildString {
                    if (title != null) {
                        append(getString(R.string.intent_title_format, title))
                    }
                    if (text != null) {
                        if (isNotEmpty()) append("\n")
                        append(getString(R.string.intent_text_format, text))
                    }
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent()
    }




}
