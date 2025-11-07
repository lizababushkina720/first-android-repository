package com.example.hw_03.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.hw_03.Keys

class NotificationReplyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val replyText = RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence(Keys.KEY_REPLY_TEXT)
            ?.toString()

        if (replyText.isNullOrBlank()) return

        val notificationId = intent.getIntExtra(Keys.EXTRA_NOTIFICATION_ID, Keys.INVALID_NOTIFICATION_ID)

        saveMessage(context, replyText)

        if (notificationId != Keys.INVALID_NOTIFICATION_ID) {
            NotificationManagerCompat.from(context).cancel(notificationId)
        }
    }

    private fun saveMessage(context: Context, message: String) {
        val prefs = context.getSharedPreferences(Keys.PREFS_NAME, Context.MODE_PRIVATE)
        val existingMessages = prefs.getString(Keys.KEY_MESSAGES, Keys.EMPTY_STRING) ?: Keys.EMPTY_STRING

        val newMessages = if (existingMessages.isEmpty()) {
            message
        } else {
            "$existingMessages${Keys.DELIMITER}$message"
        }

        prefs.edit().putString(Keys.KEY_MESSAGES, newMessages).apply()
    }

    companion object {


        fun saveMessagePublic(context: Context, message: String) {
            val prefs = context.getSharedPreferences(Keys.PREFS_NAME, Context.MODE_PRIVATE)
            val existingMessages = prefs.getString(Keys.KEY_MESSAGES, Keys.EMPTY_STRING) ?: Keys.EMPTY_STRING

            val newMessages = if (existingMessages.isEmpty()) {
                message
            } else {
                "$existingMessages${Keys.DELIMITER}$message"
            }

            prefs.edit().putString(Keys.KEY_MESSAGES, newMessages).apply()
        }

        fun getMessages(context: Context): List<String> {
            val prefs = context.getSharedPreferences(Keys.PREFS_NAME, Context.MODE_PRIVATE)
            val messagesString = prefs.getString(Keys.KEY_MESSAGES, Keys.EMPTY_STRING) ?: Keys.EMPTY_STRING

            return if (messagesString.isEmpty()) {
                emptyList()
            } else {
                messagesString.split(Keys.DELIMITER)
            }
        }

        fun clearMessages(context: Context) {
            val prefs = context.getSharedPreferences(Keys.PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }
    }
}
