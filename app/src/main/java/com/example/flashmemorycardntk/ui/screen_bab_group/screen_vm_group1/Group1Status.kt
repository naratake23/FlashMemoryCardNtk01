package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group1

import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.entities.GroupWithCardCount

data class Group1Status(
    val groupList: List<GroupWithCardCount> = emptyList(),
    val groupSize: Int = 0,
    val showDialog: Boolean = false,
    val selectionMode: Boolean = false,
    val selectedItems: Set<GroupEntity> = emptySet()
)