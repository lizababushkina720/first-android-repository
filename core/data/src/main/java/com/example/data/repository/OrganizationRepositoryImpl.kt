package com.example.data.repository

import com.example.data.mapper.OrganizationModelMapper
import com.example.data.model.OrganizationDataModel
import com.example.domain.model.OrganizationModel
import com.example.domain.repository.OrganizationRepository
import com.example.network.DaDataApi
import com.example.network.pojo.request.DaDataRequest
import javax.inject.Inject

class OrganizationRepositoryImpl @Inject constructor(
    private val daDataApi: DaDataApi,
    private val organizationModelMapper: OrganizationModelMapper,
    private val cacheRepository: OrganizationCacheRepository
) : OrganizationRepository {

    override suspend fun getOrganizationsByQuery(query: String): Pair<List<OrganizationModel>, Boolean> {

        val cached = cacheRepository.getCachedOrganizations(query)
        if (cached != null) {
            return cached.map { it.toDomain() } to true
        }

        val request = DaDataRequest(query = query)
        val response = daDataApi.suggestParty(request)

        val dataOrganizations = organizationModelMapper.map(response)

        cacheRepository.cacheOrganizations(query, dataOrganizations)

        return dataOrganizations.map { it.toDomain() } to false
    }

    override suspend fun getOrganizationByInn(inn: String): OrganizationModel? {
        val request = DaDataRequest(query = inn)
        val response = daDataApi.suggestParty(request)

        val dataOrganizations = organizationModelMapper.map(response)

        return dataOrganizations
            .map { it.toDomain() }
            .firstOrNull { it.inn == inn }
    }
    private fun OrganizationDataModel.toDomain(): OrganizationModel {
        return OrganizationModel(
            inn = this.inn,
            kpp = this.kpp.orEmpty(),
            ogrn = this.ogrn.orEmpty(),
            shortName = this.shortName,
            fullName = this.fullName,
            address = this.address.orEmpty(),
            managementName = this.managementName.orEmpty(),
            managementPost = this.managementPost.orEmpty(),
            status = this.status.orEmpty(),
            type = this.type.orEmpty()
        )
    }
}


