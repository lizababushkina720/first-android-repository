package com.example.domain.repository

import com.example.domain.model.OrganizationModel

interface OrganizationRepository {

    suspend fun getOrganizationsByQuery(query: String): Pair<List<OrganizationModel>, Boolean>
    suspend fun getOrganizationByInn(inn: String): OrganizationModel?
}