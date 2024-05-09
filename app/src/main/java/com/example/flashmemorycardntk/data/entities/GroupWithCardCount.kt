package com.example.flashmemorycardntk.data.entities

import androidx.room.Embedded

data class GroupWithCardCount(
    @Embedded val group: GroupEntity,
    val cardCount: Int
)