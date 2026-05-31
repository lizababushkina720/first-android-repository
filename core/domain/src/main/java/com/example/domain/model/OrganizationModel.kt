package com.example.domain.model


data class OrganizationModel(
    val inn: String,
    val kpp: String,
    val ogrn: String,
    val shortName: String,
    val fullName: String,
    val address: String,
    val managementName: String,
    val managementPost: String,
    val status: String,
    val type: String
)