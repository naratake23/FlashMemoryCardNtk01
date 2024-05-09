package com.example.flashmemorycardntk.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "card_entity", foreignKeys = [
    ForeignKey(
        entity = GroupEntity::class,
        parentColumns = ["groupId"],
        childColumns = ["groupId"],
        onDelete = ForeignKey.CASCADE //essa configuração garante que, quando um GroupEntity for
        // excluído, os cards associados tb serão excluídos
    )
])
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0,
    val question: String,
    val answer: String,
    val groupId: Long, // Chave estrangeira que referencia o ID do Grupo
    val isMarkedForDeletion: Boolean = false,
    val isMarkedForPlay: Boolean = false,
    val successRate: Int,
    val errorRate: Int,
    val difficultyLevel: String,
)