package com.example.flashmemorycardntk.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sequence_entity",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["cardId"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["groupId", "cardId", "orderInGroup"], unique = true)]
)
data class SequenceEntity(
    @PrimaryKey(autoGenerate = true)
    val sequenceId: Long = 0,
    val groupId: Long,
    val cardId: Long,
    val orderInGroup: Int,
)