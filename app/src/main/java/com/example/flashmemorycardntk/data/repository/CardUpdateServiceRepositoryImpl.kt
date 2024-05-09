package com.example.flashmemorycardntk.data.repository

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class CardUpdateServiceRepositoryImpl @Inject constructor() :
    CardUpdateServiceRepository {

    private val _updateServiceComplete = MutableSharedFlow<Unit>()
    override val updateServiceComplete = _updateServiceComplete.asSharedFlow()

    private val _updateServiceDeleteG1 = MutableSharedFlow<Unit>()
    override val updateServiceDeleteG1 = _updateServiceDeleteG1.asSharedFlow()


    override suspend fun updateServiceCards() {
        _updateServiceComplete.emit(Unit)
    }
    override suspend fun updateServiceDeleteG1() {
        _updateServiceDeleteG1.emit(Unit)
    }
}