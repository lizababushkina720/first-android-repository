package com.example.hw_03.model


import androidx.annotation.DrawableRes


data class NotificationModel(
    val id: Int,
    val title: String,
    val content: String? = null,
    @DrawableRes
    val icon: Int,
    val priority: NotificationPriority = NotificationPriority.DEFAULT,
    val isExpandable: Boolean = false,
    val openOnClick: Boolean = false,
    val hasReplyAction: Boolean = false
)
