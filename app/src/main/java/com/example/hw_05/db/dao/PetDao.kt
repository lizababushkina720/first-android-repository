package com.example.hw_05.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hw_05.db.entity.PetEntity

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pet: PetEntity): Long

    @Query("SELECT * FROM pets ORDER BY appeared_at DESC")
    fun getNewest(): List<PetEntity>

    @Query("SELECT * FROM pets ORDER BY weight_kg ASC, name COLLATE NOCASE ASC")
    fun getByWeight(): List<PetEntity>

    @Query("SELECT * FROM pets ORDER BY name COLLATE NOCASE ASC")
    fun getByName(): List<PetEntity>

}
