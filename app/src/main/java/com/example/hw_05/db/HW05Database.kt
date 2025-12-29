package com.example.hw_05.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hw_05.db.dao.PetDao
import com.example.hw_05.db.dao.UserDao
import com.example.hw_05.db.entity.UserEntity
import com.example.hw_05.db.entity.PetEntity
import com.example.hw_05.db.typeconverter.HW05Converters


@Database(
    entities = [UserEntity::class, PetEntity::class],
    version = 2
)
@TypeConverters(HW05Converters::class)
abstract class HW05Database : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun petDao(): PetDao
}