package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.OrganizationCacheEntity

@Dao
interface OrganizationCacheDao {
    @Query("SELECT * FROM organization_cache WHERE query = :query")
    suspend fun getByQuery(query: String): OrganizationCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: OrganizationCacheEntity)

    @Query("DELETE FROM organization_cache")
    suspend fun clearAll()
}
