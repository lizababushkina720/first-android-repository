package com.example.hw_03.utils


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.hw_03.Keys
import com.example.hw_03.MainActivity
import com.example.hw_03.R
import com.example.hw_03.model.NotificationModel
import com.example.hw_03.receiver.NotificationReplyReceiver



class NotificationHandler(private val context: Context) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val resourcesManager = ResourcesManager(context)

    private val activeNotifications = mutableMapOf<Int, NotificationModel>()

    init {
        createNotificationChannelIfNeeded()
    }
    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = resourcesManager.getString(R.string.channel_default_name)
            val channelDescription = resourcesManager.getString(R.string.channel_default_description)

            val channel = NotificationChannel(
                Keys.DEFAULT_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDescription
                enableLights(true)
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }


    fun showNotification(notificationData: NotificationModel): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        activeNotifications[notificationData.id] = notificationData

        val builder = NotificationCompat.Builder(context, Keys.DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notificationData.title)
            .setPriority(notificationData.priority.priority)
            .setAutoCancel(true)

        notificationData.content?.let { content ->
            builder.setContentText(content)

            if (notificationData.isExpandable) {
                builder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(content)
                )
            }
        }

        if (notificationData.openOnClick) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra(Keys.EXTRA_NOTIFICATION_TITLE, notificationData.title)
                putExtra(Keys.EXTRA_NOTIFICATION_TEXT, notificationData.content)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                notificationData.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            builder.setContentIntent(pendingIntent)
        }

        if (notificationData.hasReplyAction) {
            val replyLabel = resourcesManager.getString(R.string.reply_action_label)
            val remoteInput = RemoteInput.Builder(Keys.KEY_REPLY_TEXT)
                .setLabel(resourcesManager.getString(R.string.reply_input_hint))
                .build()

            val replyIntent = Intent(context, NotificationReplyReceiver::class.java).apply {
                action = Keys.ACTION_REPLY
                putExtra(Keys.EXTRA_NOTIFICATION_ID, notificationData.id)
            }

            val replyPendingIntent = PendingIntent.getBroadcast(
                context,
                notificationData.id,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            val replyAction = NotificationCompat.Action.Builder(
                R.drawable.ic_reply,
                replyLabel,
                replyPendingIntent
            ).addRemoteInput(remoteInput).build()

            builder.addAction(replyAction)
        }
        notificationManager.notify(notificationData.id, builder.build())
        return true
    }



    fun updateNotification(notificationId: Int, newText: String): Boolean {
        val existingNotification = activeNotifications[notificationId]
        if (existingNotification == null) {
            return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isActuallyActive = notificationManager.activeNotifications
                .any { it.id == notificationId }

            if (!isActuallyActive) {
                activeNotifications.remove(notificationId)
                return false
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        val updatedNotification = existingNotification.copy(content = newText)
        activeNotifications[notificationId] = updatedNotification

        return showNotification(updatedNotification)
    }


    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
        activeNotifications.remove(notificationId)
    }


    fun cancelAllNotifications(): Boolean {
        val hadNotifications = activeNotifications.isNotEmpty()
        notificationManager.cancelAll()
        activeNotifications.clear()
        return hadNotifications
    }

    fun hasNotification(notificationId: Int): Boolean {
        return activeNotifications.containsKey(notificationId)
    }
}
