package com.example.hw_03.model


import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


enum class NotificationPriority(
    val importance: Int,
    val priority: Int
) {
    MIN(
        importance = NotificationManagerCompat.IMPORTANCE_MIN,
        priority = NotificationCompat.PRIORITY_MIN
    ),
    LOW(
        importance = NotificationManagerCompat.IMPORTANCE_LOW,
        priority = NotificationCompat.PRIORITY_LOW
    ),
    DEFAULT(
        importance = NotificationManagerCompat.IMPORTANCE_DEFAULT,
        priority = NotificationCompat.PRIORITY_DEFAULT
    ),
    HIGH(
        importance = NotificationManagerCompat.IMPORTANCE_HIGH,
        priority = NotificationCompat.PRIORITY_HIGH
    )
}
