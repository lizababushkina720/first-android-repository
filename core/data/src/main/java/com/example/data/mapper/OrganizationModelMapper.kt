package com.example.data.mapper

import com.example.data.model.OrganizationDataModel
import com.example.domain.model.OrganizationModel
import com.example.network.pojo.DaDataResponse
import com.example.network.pojo.DaDataSuggestion

class OrganizationModelMapper {

    fun map(response: DaDataResponse): List<OrganizationModel> {
        return response.suggestions?.map { suggestion ->
            mapSuggestionToModel(suggestion)
        } ?: emptyList()
    }

    private fun mapSuggestionToModel(suggestion: DaDataSuggestion): OrganizationModel {
        val data = suggestion.data

        return OrganizationModel(
            inn = data?.inn.orEmpty(),
            kpp = data?.kpp.orEmpty(),
            ogrn = data?.ogrn.orEmpty(),
            shortName = data?.name?.short.orEmpty(),
            fullName = data?.name?.full.orEmpty(),
            address = data?.address?.value.orEmpty(),
            managementName = data?.management?.name.orEmpty(),
            managementPost = data?.management?.post.orEmpty(),
            status = data?.state?.status.orEmpty(),
            type = data?.type.orEmpty()
        )
    }



}