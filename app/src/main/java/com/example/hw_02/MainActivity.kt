package com.example.hw_02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.hw_02.ui.navigation.AppNavigation
import com.example.hw_02.ui.theme.NotesAppTheme
import com.example.hw_02.ui.theme.ThemeType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var currentTheme by remember { mutableStateOf(ThemeType.DEFAULT) }

            NotesAppTheme(themeType = currentTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        onThemeChange = { newTheme ->
                            currentTheme = newTheme
                        }
                    )
                }
            }
        }
    }
}
