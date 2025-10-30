package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class ThirdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textReceived = intent.getStringExtra("TEXT_KEY") ?: "третий экран"
        setContent {
            ThirdScreen(textReceived)

        }
    }

    @Composable
    fun ThirdScreen(receivedText: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = receivedText, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(100.dp))
            Button(onClick = {
                val intent = Intent(this@ThirdActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }) {
                Text("на первый")
            }
        }
    }
}