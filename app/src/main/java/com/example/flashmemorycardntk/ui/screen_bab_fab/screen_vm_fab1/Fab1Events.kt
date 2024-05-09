package com.example.flashmemorycardntk.ui.screen_bab_fab.screen_vm_fab1

import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.SCDifficulty

sealed class Fab1Events {
    object ButtonDropDownDialogClicked : Fab1Events()
    object CancelDialog : Fab1Events()
    data class DropDownDialogItemSelected(val groupSelected: GroupEntity) : Fab1Events()
    data class SegmentedButtonClicked(val toggle: EnumQuestionAnswer) : Fab1Events()
    data class ButtonLevelClicked(val level: SCDifficulty) : Fab1Events()
    data class ButtonSaveClicked(val cardEntity: CardEntity) : Fab1Events()
}