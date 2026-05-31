package com.example.hw_01_sem2.fcm


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.hw_01_sem2.MainActivity
import com.example.hw_01_sem2.R

class AppMessagingService : FirebaseMessagingService() {
    companion object {
        private const val KEY_KIND = "kind"
        private const val KEY_TITLE = "title"
        private const val KEY_BODY = "body"
        private const val KEY_ID = "id"
        private const val KEY_ORG_INN = "organizationInn"

        private const val KIND_ORGANIZATION_UPDATE = "organization_update"
        private const val KIND_NEW_TENDER = "new_tender"

        private const val CHANNEL_ID = "main_notifications_channel"

        const val EXTRA_ORGANIZATION_INN = "organizationInn"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data

        val kind = data[KEY_KIND] ?: return

        val title = data[KEY_TITLE] ?: getString(R.string.notification_default_title)
        val body = data[KEY_BODY] ?: ""
        val notificationId = data[KEY_ID]?.toIntOrNull() ?: System.currentTimeMillis().toInt()

        when (kind) {
            KIND_ORGANIZATION_UPDATE -> {
                val inn = data[KEY_ORG_INN]
                sendNotification(notificationId, title, body, inn)
            }
            KIND_NEW_TENDER -> {
                sendNotification(notificationId, title, body, null)
            }
            else -> {
                sendNotification(notificationId, title, body, null)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("FCM Token: $token")
    }

    private fun sendNotification(id: Int, title: String, body: String, organizationInn: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (organizationInn != null) {
                putExtra(EXTRA_ORGANIZATION_INN, organizationInn)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(id, notificationBuilder.build())
    }
}