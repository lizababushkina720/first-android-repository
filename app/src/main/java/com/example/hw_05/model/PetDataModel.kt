package com.example.hw_05.model


import java.util.Date

data class PetDataModel(
    val id: Long,
    val name: String,
    val weightKg: Double,
    val appearedAt: Date
)
