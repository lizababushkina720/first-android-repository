package com.example.data.repository

import com.example.data.local.dao.OrganizationCacheDao
import com.example.data.local.entity.OrganizationCacheEntity
import com.example.domain.model.OrganizationModel
import com.google.gson.Gson

class OrganizationCacheRepository(
    private val dao: OrganizationCacheDao,
    private val gson: Gson,
    private val ttlMillis: Long = 30_000L
) {

    suspend fun getCachedOrganizations(query: String): List<OrganizationModel>? {
        val entity = dao.getByQuery(query) ?: return null
        val now = System.currentTimeMillis()
        if (now - entity.timestampMillis > ttlMillis) {
            return null
        }
        val type = object : com.google.gson.reflect.TypeToken<List<OrganizationModel>>() {}.type
        return gson.fromJson(entity.organizationsJson, type)
    }

    suspend fun cacheOrganizations(query: String, organizations: List<OrganizationModel>) {
        val json = gson.toJson(organizations)
        val entity = OrganizationCacheEntity(
            query = query,
            organizationsJson = json,
            timestampMillis = System.currentTimeMillis()
        )
        dao.insert(entity)
    }
}
