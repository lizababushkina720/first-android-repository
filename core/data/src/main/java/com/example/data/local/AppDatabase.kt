package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.OrganizationCacheDao
import com.example.data.local.entity.OrganizationCacheEntity

@Database(
    entities = [OrganizationCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun organizationCacheDao(): OrganizationCacheDao
}
