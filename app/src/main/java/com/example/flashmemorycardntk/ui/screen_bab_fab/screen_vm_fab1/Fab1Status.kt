package com.example.flashmemorycardntk.ui.screen_bab_fab.screen_vm_fab1

import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.SCDifficulty

data class Fab1Status(
    val groupList: List<GroupEntity> = emptyList(),
    val showDialog: Boolean = false,
    val isEmptyGroupList: Boolean = false,
    val groupSelectedInDialog: GroupEntity = GroupEntity(name = "EMPTY fs"),
    val segmentedButtonStatus: EnumQuestionAnswer = EnumQuestionAnswer.QUESTION,
    val levelSelected: SCDifficulty = SCDifficulty.Level1,
)