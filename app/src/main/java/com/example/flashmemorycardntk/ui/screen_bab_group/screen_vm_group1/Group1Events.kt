package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group1

sealed class Group1Events {
    object ButtonAddClicked : Group1Events()
    data class ButtonSaveClicked(val groupName: String) : Group1Events()
    object CancelDialog : Group1Events()
    object ButtonDeleteClicked : Group1Events()
}