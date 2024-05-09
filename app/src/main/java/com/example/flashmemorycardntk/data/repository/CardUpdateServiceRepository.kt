package com.example.flashmemorycardntk.data.repository

import kotlinx.coroutines.flow.SharedFlow

interface CardUpdateServiceRepository {

    val updateServiceComplete: SharedFlow<Unit>
    val updateServiceDeleteG1: SharedFlow<Unit>

    suspend fun updateServiceCards()
    suspend fun updateServiceDeleteG1()
}