package com.example.hw_05.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hw_05.Keys
import com.example.hw_05.R
import com.example.hw_05.db.HW05Database
import com.example.hw_05.db.entity.PetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class SeedDataCallback(
    private val appContext: Context,
    private val dbProvider: () -> HW05Database,
    private val appScope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        appScope.launch(Dispatchers.IO) {
            val dbInstance = dbProvider()
            val petDao = dbInstance.petDao()


            val names = appContext.resources.getStringArray(R.array.seed_pet_names_by_oldest)
            val weights = appContext.resources.getStringArray(R.array.seed_pet_weights_by_oldest)

            val count = minOf(names.size, weights.size)
            val now = System.currentTimeMillis()
            val dayMs = Keys.DAY_MS


            for (i in 0 until count) {
                val w = weights[i].trim().replace(',', '.').toDoubleOrNull() ?: continue

                val appearedAt = Date(now - (count - 1L - i) * dayMs)

                petDao.insert(
                    PetEntity(
                        name = names[i],
                        weightKg = w,
                        appearedAt = appearedAt
                    )
                )
            }
        }
    }
}
