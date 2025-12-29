package com.example.hw_05.di

import android.content.Context
import androidx.room.Room
import com.example.hw_05.db.HW05Database
import kotlinx.coroutines.Dispatchers

import com.example.hw_05.Keys
import com.example.hw_05.data.PetRepository
import com.example.hw_05.data.UserRepository
import com.example.hw_05.mapper.PetModelMapper
import com.example.hw_05.mapper.UserModelMapper

object ServiceLocator {

    private var hw05Database: HW05Database? = null

    private val userModelMapper = UserModelMapper()
    private val petModelMapper = PetModelMapper()

    val userRepository by lazy {
        UserRepository(
            userDao = getDatabase().userDao(),
            mapper = userModelMapper,
            ioDispatcher = Dispatchers.IO
        )
    }

    val petRepository by lazy {
        PetRepository(
            petDao = getDatabase().petDao(),
            mapper = petModelMapper,
            ioDispatcher = Dispatchers.IO
        )
    }

    fun initDatabase(appCtx: Context) {
        val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.SupervisorJob())

        hw05Database =
            Room.databaseBuilder(appCtx, HW05Database::class.java, Keys.DB_NAME)
                .addCallback(
                    com.example.hw_05.db.SeedDataCallback(
                        appContext = appCtx.applicationContext,
                        dbProvider = { getDatabase() },
                        appScope = scope
                    )
                )
                .fallbackToDestructiveMigration(true)
                .build()
    }


    fun getDatabase(): HW05Database =
        hw05Database ?: throw IllegalStateException("DB is not initialized")

}
