package com.example.hw_05.data


import com.example.hw_05.Keys
import com.example.hw_05.db.dao.PetDao
import com.example.hw_05.mapper.PetModelMapper
import com.example.hw_05.model.PetDataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PetRepository(
    private val petDao: PetDao,
    private val mapper: PetModelMapper,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun addPet( pet: PetDataModel): Long =
        withContext(ioDispatcher) {
            petDao.insert(mapper.map(pet))
        }

    suspend fun getPets( sort: String): List<PetDataModel> =
        withContext(ioDispatcher) {
            val list = when (sort) {
                Keys.SORT_WEIGHT -> petDao.getByWeight()
                Keys.SORT_ALPHA -> petDao.getByName()
                else -> petDao.getNewest()
            }
            list.map { mapper.map(it) }
        }
}
