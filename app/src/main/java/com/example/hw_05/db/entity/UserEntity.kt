package com.example.hw_05.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "nickname") val nickname: String,

    @ColumnInfo(name = "email") val email: String,

    @ColumnInfo(name = "password_hash") val passwordHash: String,

    @ColumnInfo(name = "deleted_at") val deletedAt: Long? = null
)