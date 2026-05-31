package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "organization_cache")
data class OrganizationCacheEntity(
    @PrimaryKey val query: String,
    val organizationsJson: String,
    val timestampMillis: Long
)
