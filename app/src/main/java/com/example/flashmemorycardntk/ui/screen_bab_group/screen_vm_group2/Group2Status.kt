package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2

import com.example.flashmemorycardntk.data.entities.CardEntity

data class Group2Status (
    val cardList: List<CardEntity> = emptyList(),
    val groupName: String = "xxt g2s",
    val showDialogEditGroupName: Boolean = false,
    val selectionMode: Boolean = false,
    val selectedItems: Set<CardEntity> = emptySet()
)