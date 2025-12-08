package com.example.hw_04.model


data class CoroutineResult(
    val id: Int,
    val executionTime: Long,
    val isSuccess: Boolean,
    val errorMessage: String = ""
)
