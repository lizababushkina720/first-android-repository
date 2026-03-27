package com.example.data.model

data class OrganizationDataModel(
    val inn: String,
    val kpp: String?,
    val ogrn: String?,
    val shortName: String,
    val fullName: String,
    val address: String?,
    val managementName: String?,
    val managementPost: String?,
    val status: String?,
    val type: String?
) {

    companion object {
        val EMPTY = OrganizationDataModel(
            inn = "",
            kpp = null,
            ogrn = null,
            shortName = "",
            fullName = "",
            address = null,
            managementName = null,
            managementPost = null,
            status = null,
            type = null
        )
    }
}