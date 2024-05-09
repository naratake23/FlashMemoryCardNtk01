package com.example.flashmemorycardntk.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashmemorycardntk.model.EnumSortSequence

@Entity(tableName = "group_entity")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId: Long = 0,
    val name: String,
    val isMarkedForDeletion: Boolean = false,
    val settingInfinite: Boolean = false,
    val settingSortSequence: String = EnumSortSequence.ORIGINAL.strEnum,
    val settingShuffle: Boolean = false,
    val settingBalance: Int = 3,
)