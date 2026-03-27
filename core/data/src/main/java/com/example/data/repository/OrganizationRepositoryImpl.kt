package com.example.data.repository

import com.example.data.mapper.OrganizationModelMapper
import com.example.domain.model.OrganizationModel
import com.example.domain.repository.OrganizationRepository
import com.example.network.DaDataApi
import com.example.network.pojo.request.DaDataRequest

class OrganizationRepositoryImpl(
    private val daDataApi: DaDataApi,
    private val organizationModelMapper: OrganizationModelMapper,
    private val cacheRepository: OrganizationCacheRepository
) : OrganizationRepository {
    private var requestCount = 0

    override suspend fun getOrganizationsByQuery(query: String): Pair<List<OrganizationModel>, Boolean> {


        val cached = cacheRepository.getCachedOrganizations(query)
        if (cached != null) {
            return cached to true
        }
        requestCount++
        if (requestCount % 3 == 0) {
            throw Exception("ошибка 404")
        }


        val request = DaDataRequest(query = query)
        val response = daDataApi.suggestParty(request)
        val organizations = organizationModelMapper.map(response)


        cacheRepository.cacheOrganizations(query, organizations)

        return organizations to false
    }
}




