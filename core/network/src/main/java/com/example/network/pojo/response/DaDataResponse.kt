package com.example.network.pojo.response

import com.google.gson.annotations.SerializedName

data class DaDataResponse(
    @SerializedName("suggestions")
    val suggestions: List<DaDataSuggestion>?
)

data class DaDataSuggestion(
    @SerializedName("value")
    val value: String?,

    @SerializedName("unrestricted_value")
    val unrestrictedValue: String?,

    @SerializedName("data")
    val data: DaDataParty?
)

data class DaDataParty(
    @SerializedName("inn")
    val inn: String?,

    @SerializedName("kpp")
    val kpp: String?,

    @SerializedName("ogrn")
    val ogrn: String?,

    @SerializedName("name")
    val name: DaDataName?,

    @SerializedName("address")
    val address: DaDataAddress?,

    @SerializedName("management")
    val management: DaDataManagement?,

    @SerializedName("state")
    val state: DaDataState?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("branch_type")
    val branchType: String?,

    @SerializedName("branch_count")
    val branchCount: Int?,

    @SerializedName("okved")
    val okved: String?,

)

data class DaDataName(
    @SerializedName("short")
    val short: String?,

    @SerializedName("full")
    val full: String?
)

data class DaDataAddress(
    @SerializedName("value")
    val value: String?,

    @SerializedName("unrestricted_value")
    val unrestrictedValue: String?,

    @SerializedName("data")
    val data: DaDataAddressData?
)

data class DaDataAddressData(
    @SerializedName("postal_code")
    val postalCode: String?,

    @SerializedName("city")
    val city: String?,

    @SerializedName("street_with_type")
    val streetWithType: String?
)

data class DaDataManagement(
    @SerializedName("name")
    val name: String?,

    @SerializedName("post")
    val post: String?
)

data class DaDataState(
    @SerializedName("status")
    val status: String?,

    @SerializedName("actuality_date")
    val actualityDate: Long?
)