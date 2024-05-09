package com.example.flashmemorycardntk.ui.screen_bab_shuffle.screen_vm_shuffle1

import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.model.EnumSortSequence

sealed class Shuffle1Events {
    object ButtonDropDownDialogGroupClicked : Shuffle1Events()
    data class DropDownDialogGroupItemSelected(val groupSelected: GroupEntity) : Shuffle1Events()
    object CancelDialogs : Shuffle1Events()
    data class InfinitySwitchClicked(val isSelected: Boolean) : Shuffle1Events()
    object ButtonDropDownDialogOrderClicked : Shuffle1Events()
    data class DropDownDialogOrderItemSelected(val orderSelected: EnumSortSequence) : Shuffle1Events()
    data class ShuffleSwitchClicked(val isSelected: Boolean) : Shuffle1Events()
    data class BalanceSliderClicked(val ratio: Int) : Shuffle1Events()
    object ButtonApplyClicked : Shuffle1Events()

}