package com.example.flashmemorycardntk.data.repository

import com.example.flashmemorycardntk.data.dao.CardDao
import com.example.flashmemorycardntk.data.entities.CardEntity
import kotlinx.coroutines.flow.Flow

class CardRepositoryImpl(private val cardDao: CardDao) {

    suspend fun insertCardRepo(card: CardEntity) {
        cardDao.insertCard(card)
    }

    suspend fun insertCardListRepo(cardList: List<CardEntity>) {
        cardDao.insertCardList(cardList)
    }

    suspend fun updateCardRepo(card: CardEntity) {
        cardDao.updateCard(card)
    }

    suspend fun updateCardListRepo(cardList: List<CardEntity>) {
        cardDao.updateCardList(cardList)
    }

    suspend fun deleteCardListRepo(cardList: List<CardEntity>) {
        cardDao.deleteCardList(cardList)
    }

    suspend fun getCardByIdRepo(cardId: Long): CardEntity {
        return cardDao.getCardById(cardId)
    }

    fun getCardListByGroupIdRepo(groupId: Long): Flow<List<CardEntity>> {
        return cardDao.getCardListByGroupId(groupId)
    }

    suspend fun incrementSuccessRateRepo(cardId: Long) {
        cardDao.incrementSuccessRate(cardId)
    }

    suspend fun incrementErrorRateRepo(cardId: Long) {
        cardDao.incrementErrorRate(cardId)
    }
}