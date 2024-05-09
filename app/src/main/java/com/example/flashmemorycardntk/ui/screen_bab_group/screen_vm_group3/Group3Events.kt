package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group3

import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.SCDifficulty

sealed class Group3Events {
    data class SegmentedButtonClicked(val toggle: EnumQuestionAnswer) : Group3Events()
    data class ButtonLevelClicked(val level: SCDifficulty) : Group3Events()
    data class ButtonSaveClicked(val question: String, val answer: String) : Group3Events()
}