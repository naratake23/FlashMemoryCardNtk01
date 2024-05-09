package com.example.flashmemorycardntk.ui.screen_bab_play.screen_vm_play1

import com.example.flashmemorycardntk.data.entities.CardEntity

sealed class Play1Events {
    object DoubleClicked : Play1Events()
    data class SwipedRight(val card: CardEntity) : Play1Events()
    data class SwipedLeft(val card: CardEntity) : Play1Events()
    object RetakeButtonClicked : Play1Events()
}