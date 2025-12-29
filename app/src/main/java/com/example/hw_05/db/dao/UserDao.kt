package com.example.hw_05.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hw_05.db.entity.UserEntity


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun findById(id: Long): UserEntity?

    @Query("UPDATE users SET deleted_at = :deletedAt WHERE id = :id")
    fun softDelete(id: Long, deletedAt: Long)

    @Query("UPDATE users SET deleted_at = NULL WHERE id = :id")
    fun restore(id: Long)

    @Query("DELETE FROM users WHERE id = :id")
    fun hardDelete(id: Long)

    @Query("DELETE FROM users WHERE deleted_at IS NOT NULL AND deleted_at < :borderMs")
    fun cleanupDeleted(borderMs: Long): Int

    @Query("UPDATE users SET deleted_at = NULL WHERE email = :email")
    fun restoreByEmail(email: String): Int

}