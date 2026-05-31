package com.example.data.mapper

import com.example.data.model.OrganizationDataModel
import com.example.network.pojo.response.DaDataResponse
import com.example.network.pojo.response.DaDataSuggestion
import javax.inject.Inject

class OrganizationModelMapper @Inject constructor(){

    fun map(response: DaDataResponse): List<OrganizationDataModel> {
        return response.suggestions?.map { suggestion ->
            mapSuggestionToModel(suggestion)
        } ?: emptyList()
    }

    private fun mapSuggestionToModel(suggestion: DaDataSuggestion): OrganizationDataModel {
        val data = suggestion.data

        return OrganizationDataModel(
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