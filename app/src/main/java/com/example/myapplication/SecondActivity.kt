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

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textReceived = intent.getStringExtra("TEXT_KEY") ?: "секонд экран"
        setContent {
            SecondScreen(textReceived)
        }
    }

    @Composable
    fun SecondScreen(receivedText: String) {
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
                val intent = Intent(this@SecondActivity, ThirdActivity::class.java)
                val textToSend = this@SecondActivity.intent.getStringExtra("TEXT_KEY")
                if (textToSend != null) {
                    intent.putExtra("TEXT_KEY", textToSend)
                }
                startActivity(intent)
            }) {
                Text("go to третий экран")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                val intent = Intent(this@SecondActivity, MainActivity::class.java)
                startActivity(intent)
            }) {
                Text("на первый")
            }
        }
    }
}