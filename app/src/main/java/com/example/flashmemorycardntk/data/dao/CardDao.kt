package com.example.flashmemorycardntk.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashmemorycardntk.data.entities.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert
    suspend fun insertCard(cardEntity: CardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardList(cardList: List<CardEntity>)

    @Update
    suspend fun updateCard(cardEntity: CardEntity)

    @Update
    suspend fun updateCardList(cardList: List<CardEntity>)

    @Delete
    suspend fun deleteCardList(cardList: List<CardEntity>)

    @Query("SELECT * FROM card_entity WHERE cardId = :cardId")
    suspend fun getCardById(cardId: Long): CardEntity

    @Query("SELECT * FROM card_entity WHERE groupId = :groupId")
    fun getCardListByGroupId(groupId: Long): Flow<List<CardEntity>>



    @Query("UPDATE card_entity SET successRate = successRate + 1 WHERE cardId = :cardId")
    suspend fun incrementSuccessRate(cardId: Long)

    @Query("UPDATE card_entity SET errorRate = errorRate + 1 WHERE cardId = :cardId")
    suspend fun incrementErrorRate(cardId: Long)
}