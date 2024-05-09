package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2

sealed class Group2Events {
    object ButtonEditGroupNameClicked : Group2Events()
    data class ButtonDialogEditNameClicked(val groupNewName: String) : Group2Events()
    object ButtonDeleteClicked : Group2Events()
    data class ButtonSaveMultipleCardsClicked(val text: String) : Group2Events()
    object CancelDialog : Group2Events()
}