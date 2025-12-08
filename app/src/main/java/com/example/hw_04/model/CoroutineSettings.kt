package com.example.hw_04.model


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


data class CoroutineSettings(
    val count: Int = 10,
    val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    val isSequential: Boolean = true,
    val isParallel: Boolean = false,
    val isLazy: Boolean = false,
    val isBackgroundWork: Boolean = true
)
