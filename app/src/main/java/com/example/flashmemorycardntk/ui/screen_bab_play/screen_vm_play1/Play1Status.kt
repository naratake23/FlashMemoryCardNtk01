package com.example.flashmemorycardntk.ui.screen_bab_play.screen_vm_play1

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.model.SCDifficulty

data class Play1Status(
    val groupSelected: GroupEntity? = null,
    val cardList: List<CardEntity> = emptyList(),
    val cardsQty: Int = 0,
    val completedCards: Int = 0,
    val completedCardsCounter: Int = 1,
    val correctCardsCounter: Int = 0,
    val isInfinity: Boolean = false,
    val cardSelected: CardEntity? = null,
    val cardFrontSide: Boolean = true,
    val skipAnimation: Boolean = true,
    val cardLevel: SCDifficulty = SCDifficulty.Level1,
    val offsetX: MutableFloatState = mutableFloatStateOf(0f),
    val offsetY: MutableFloatState = mutableFloatStateOf(0f),
    val isLoading: Boolean = true,
    )