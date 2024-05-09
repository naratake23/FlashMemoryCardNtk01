package com.example.flashmemorycardntk.ui.screen_bab_shuffle.screen_vm_shuffle1

import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.model.EnumSortSequence

data class Shuffle1Status(
    val groupList: List<GroupEntity> = emptyList(),
    val isEmptyGroupList: Boolean = false,
    val showDialogGroup: Boolean = false,
    val groupSelectedInDialog: GroupEntity? = null,
    val infinitySwitch: Boolean = false,
    val showDialogOrder: Boolean = false,
    val orderSelectedInDialog: String = EnumSortSequence.ORIGINAL.strEnum,
    val shuffleSwitch: Boolean = false,
    val balanceSlider: Int = 3,
)