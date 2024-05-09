package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group3

import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.SCDifficulty

data class Group3Status(
    val cardSelected: CardEntity? = null,
    val groupSelected: GroupEntity? = null,
    val segmentedButtonStatus: EnumQuestionAnswer = EnumQuestionAnswer.QUESTION,
    val levelSelected: SCDifficulty = SCDifficulty.Level1,
)